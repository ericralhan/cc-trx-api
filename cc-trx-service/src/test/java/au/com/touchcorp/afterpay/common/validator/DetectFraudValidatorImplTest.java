package au.com.touchcorp.afterpay.common.validator;

import au.com.touchcorp.afterpay.cc.request.DetectFraudRequest;
import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionValidationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DetectFraudValidatorImplTest {

    private DetectFraudValidatorImpl validator;

    @Test(expected = CCTransactionValidationException.class)
    public void detectFraud_withEmptyRequest() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeEmptyRequest();

        validator.validateDetectFraudRequest(detectFraudRequest);
    }

    @Test(expected = CCTransactionValidationException.class)
    public void detectFraud_withValidTransaction() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeEmptyRequest();

        validator.validateDetectFraudRequest(detectFraudRequest);
    }

    @Test(expected = CCTransactionValidationException.class)
    public void detectFraud_withOneValidTransaction() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeRequestWithOneValidTransaction();

        validator.validateDetectFraudRequest(detectFraudRequest);
    }

    @Test(expected = CCTransactionValidationException.class)
    public void detectFraud_withOneValidTransactionAndFutureDate() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeRequestWithOneValidTransactionAndFutureDate();

        validator.validateDetectFraudRequest(detectFraudRequest);
    }

    @Test(expected = CCTransactionValidationException.class)
    public void detectFraud_withOneValidTransactionAndZeroThreshold() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeRequestWithOneValidTransaction();
        detectFraudRequest.setThreshold(0);

        validator.validateDetectFraudRequest(detectFraudRequest);
    }

    @Test
    public void detectFraud_withValidRequest() throws Exception {

        validator = new DetectFraudValidatorImpl();

        DetectFraudRequest detectFraudRequest = makeRequestWithOneValidTransaction();
        detectFraudRequest.setFraudDate(new Date());
        detectFraudRequest.setThreshold(5);

        List<CCTransactionRecord> validList = validator.validateDetectFraudRequest(detectFraudRequest);

        assertEquals(1, validList.size());
    }

    private DetectFraudRequest makeRequestWithOneValidTransactionAndFutureDate() {

        DetectFraudRequest request = new DetectFraudRequest();
        List<CCTransactionRecord> transactions = makeOneTransaction();

        request.setTransactions(transactions);

        // future date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        request.setFraudDate(cal.getTime());

        request.setThreshold(0);

        return request;
    }

    private DetectFraudRequest makeRequestWithOneValidTransaction() {

        DetectFraudRequest request = new DetectFraudRequest();
        List<CCTransactionRecord> transactions = makeOneTransaction();

        request.setTransactions(transactions);
        request.setFraudDate(new Date());
        request.setThreshold(0);

        return request;
    }

    private DetectFraudRequest makeEmptyRequest() {

        DetectFraudRequest request = new DetectFraudRequest();

        List<CCTransactionRecord> transactions = new ArrayList<>();
        request.setTransactions(transactions);
        request.setFraudDate(null);
        request.setThreshold(0);

        return request;
    }

    private List<CCTransactionRecord> makeOneTransaction() {

        List<CCTransactionRecord> transactions = new ArrayList<>();
        CCTransactionRecord transaction = new CCTransactionRecord();

        transaction.setCcHashNum("qwe");
        transaction.setTrxAmount(10);
        transaction.setTrxDate(new Date());

        transactions.add(transaction);

        return transactions;
    }

}
