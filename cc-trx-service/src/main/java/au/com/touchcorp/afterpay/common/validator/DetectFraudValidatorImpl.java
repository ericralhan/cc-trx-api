package au.com.touchcorp.afterpay.common.validator;

import au.com.touchcorp.afterpay.cc.request.DetectFraudRequest;
import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetectFraudValidatorImpl implements DetectFraudValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetectFraudValidatorImpl.class);

    @Override
    public List<CCTransactionRecord> validateDetectFraudRequest(DetectFraudRequest detectFraudRequest) throws CCTransactionValidationException {

        List<CCTransactionRecord> validTransactions = validateTransactions(detectFraudRequest.getTransactions());

        if (validTransactions.size() == 0)
            throw new CCTransactionValidationException("no valid transaction found");

        if (validateFutureDate(detectFraudRequest.getFraudDate()))
            throw new CCTransactionValidationException("date to check fraud for, cannot be in future");

        if (detectFraudRequest.getThreshold() <= 0)
            throw new CCTransactionValidationException("threshold amount for fraud should be greater than zero");

        return validTransactions;
    }

    private List<CCTransactionRecord> validateTransactions(List<CCTransactionRecord> transactions) {

        // skip transactions for which the validations fail
        return transactions.stream().filter(transaction -> {

            if (!StringUtils.isAlphanumeric(transaction.getCcHashNum()) &&
                    validateFutureDate(transaction.getTrxDate()) &&
                    (transaction.getTrxAmount() < 0)) {

                LOGGER.info("validation failed for transaction ->" + transaction.toString());
                return false;
            } else
                return true;
        }).collect(Collectors.toList());
    }

    private boolean validateFutureDate(Date fraudDate) {

        if (DateUtils.truncatedCompareTo(fraudDate, new Date(), Calendar.DATE) > 0) {
            LOGGER.info("date cannot be in future");
            return true;
        }
        return false;
    }

}
