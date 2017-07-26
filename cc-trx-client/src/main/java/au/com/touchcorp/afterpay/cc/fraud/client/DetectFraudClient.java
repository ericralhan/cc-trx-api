package au.com.touchcorp.afterpay.cc.fraud.client;

import au.com.touchcorp.afterpay.cc.fraud.service.DetectFraudService;
import au.com.touchcorp.afterpay.cc.fraud.service.utils.CCTransactionsParser;
import au.com.touchcorp.afterpay.cc.request.DetectFraudRequest;
import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionApplicationException;
import au.com.touchcorp.afterpay.common.validator.DetectFraudValidator;
import au.com.touchcorp.afterpay.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class DetectFraudClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetectFraudClient.class);

    public void execute(String trxRawString, Date fraudDate, Double threshold) throws CCTransactionApplicationException {

        // create spring context
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        DetectFraudValidator validator = ctx.getBean(DetectFraudValidator.class);
        DetectFraudService service = ctx.getBean(DetectFraudService.class);

        List<CCTransactionRecord> trxList = CCTransactionsParser.parseTransactions(trxRawString);

        // create request
        DetectFraudRequest request = new DetectFraudRequest();
        request.setTransactions(trxList);
        request.setFraudDate(fraudDate);
        request.setThreshold(threshold);

        List<CCTransactionRecord> validTransactions = validator.validateDetectFraudRequest(request);

        Set<String> fraudCCNums = service.detectFraud(validTransactions, fraudDate, threshold);

        LOGGER.debug("Set of fraud Credit Cards ");

        fraudCCNums.forEach(LOGGER::debug);
    }

}
