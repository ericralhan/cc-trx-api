package au.com.touchcorp.afterpay.cc.fraud.service;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class DetectFraudServiceImplTest {

    private DetectFraudServiceImpl detectFraudService;

    @Test
    public void detectFraud_withEmptyTransaction() throws Exception {

        List<CCTransactionRecord> transactions = new ArrayList<>();

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, null, 0);

        assertEquals(set.size(), 0);
    }

    @Test
    public void detectFraud_withOneTransactionZeroFraud() throws Exception {

        List<CCTransactionRecord> transactions = makeOneTransaction();

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, new Date(), 1);

        assertEquals(set.size(), 0);
    }

    @Test
    public void detectFraud_withOneTransactionOneFraud() throws Exception {

        List<CCTransactionRecord> transactions = makeOneTransaction();

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, new Date(), 0);

        assertEquals(set.size(), 1);
    }

    @Test
    public void detectFraud_withTwoTransactionsOneFraud() throws Exception {

        List<CCTransactionRecord> transactions = makeTwoTransactions();

        assertEquals(transactions.size(), 2);

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, new Date(), 7);

        assertEquals(set.size(), 1);
        assertEquals(set.iterator().next(), "xyz");
    }

    @Test
    public void detectFraud_withTwoTransactionsTwoFrauds() throws Exception {

        List<CCTransactionRecord> transactions = makeTwoTransactions();

        assertEquals(transactions.size(), 2);

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, new Date(), 4);

        assertEquals(set.size(), 2);
        assertTrue(set.contains("abc"));
        assertTrue(set.contains("xyz"));
    }

    @Test
    public void detectFraud_withTwoTransactionsSameCC() throws Exception {

        List<CCTransactionRecord> transactions = makeTwoTransactionsSameCC();

        assertEquals(transactions.size(), 2);

        detectFraudService = new DetectFraudServiceImpl();

        Set<String> set = detectFraudService.detectFraud(transactions, new Date(), 19);

        assertEquals(set.size(), 1);
        assertTrue(set.contains("xyz"));
    }

    private List<CCTransactionRecord> makeOneTransaction() {

        List<CCTransactionRecord> transactions = new ArrayList<>();
        CCTransactionRecord transaction = new CCTransactionRecord();

        transaction.setCcHashNum("");
        transaction.setTrxAmount(1);
        transaction.setTrxDate(new Date());

        transactions.add(transaction);

        return transactions;
    }

    private List<CCTransactionRecord> makeTwoTransactions() {

        List<CCTransactionRecord> transactions = new ArrayList<>();
        CCTransactionRecord transaction = new CCTransactionRecord();

        transaction.setCcHashNum("xyz");
        transaction.setTrxAmount(10);
        transaction.setTrxDate(new Date());

        CCTransactionRecord transaction1 = new CCTransactionRecord();

        transaction1.setCcHashNum("abc");
        transaction1.setTrxAmount(5);
        transaction1.setTrxDate(new Date());

        transactions.add(transaction);
        transactions.add(transaction1);

        return transactions;
    }

    private List<CCTransactionRecord> makeTwoTransactionsSameCC() {

        List<CCTransactionRecord> transactions = new ArrayList<>();
        CCTransactionRecord transaction = new CCTransactionRecord();

        transaction.setCcHashNum("xyz");
        transaction.setTrxAmount(10);
        transaction.setTrxDate(new Date());

        CCTransactionRecord transaction1 = new CCTransactionRecord();

        transaction1.setCcHashNum("xyz");
        transaction1.setTrxAmount(10);
        transaction1.setTrxDate(new Date());

        transactions.add(transaction);
        transactions.add(transaction1);

        return transactions;
    }

}
