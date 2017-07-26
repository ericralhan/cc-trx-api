package au.com.touchcorp.afterpay;

import au.com.touchcorp.afterpay.cc.fraud.client.DetectFraudClient;
import au.com.touchcorp.afterpay.common.exception.CCTransactionApplicationException;
import au.com.touchcorp.afterpay.config.AppConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class AppRunner {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {

        String rawString = "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00";
        Date fraudDate = new Date();
        Double threshold = 11.0;

        DetectFraudClient client = new DetectFraudClient();

        try {
            client.execute(rawString, fraudDate, threshold);

            LOGGER.debug("success");
        } catch (CCTransactionApplicationException e) {

            LOGGER.error(e.getMessage());
        }
    }
}
