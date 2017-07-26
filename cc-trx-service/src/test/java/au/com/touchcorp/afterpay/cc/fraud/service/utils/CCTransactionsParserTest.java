package au.com.touchcorp.afterpay.cc.fraud.service.utils;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionParserException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CCTransactionsParserTest {

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString1() throws Exception {

        CCTransactionsParser.parseTransactions("");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString2() throws Exception {

        CCTransactionsParser.parseTransactions("abc,");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString3() throws Exception {

        CCTransactionsParser.parseTransactions("abc,,");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString4() throws Exception {

        CCTransactionsParser.parseTransactions("abc,15-11-2011,12.53");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString5() throws Exception {

        CCTransactionsParser.parseTransactions("#$%^&*()(*&^$%^&*");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString6() throws Exception {

        CCTransactionsParser.parseTransactions("askdj,3245,234");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString7() throws Exception {

        CCTransactionsParser.parseTransactions("as, 15-12-2016, 12.53");
    }

    @Test(expected = CCTransactionParserException.class)
    public void parseTransactions_allInvalidString8() throws Exception {

        CCTransactionsParser.parseTransactions("as, 2019-04-29T22:51:54, qwe");
    }

    @Test
    public void parseTransactions_ValidStrings() throws Exception {

        List<CCTransactionRecord> list = null;

        //valid strings
        list = CCTransactionsParser.parseTransactions(",,,,,,,,");
        assertEquals(list.size(), 0);

        list = CCTransactionsParser.parseTransactions("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00");
        assertEquals(list.size(), 1);

        list = CCTransactionsParser.parseTransactions("10d7ce2f43e35fa57d1bbf8b1e23498iureqt3489, 2019-04-29T22:15:54, 4567.12");
        assertEquals(list.size(), 1);

        list = CCTransactionsParser.parseTransactions("10d7ce2f43e35fa57d1bbf8b1e23498iureqt3489, 2019-04-29T22:15:54, 4567.12,10d7asdface2f43e35fa57d1bbf8b1e23498iureqt3489, 2019-12-29T22:15:54, 13445.12,10d7ce2f43e35fa57d1bbf8b1e23498iureasdfqt3489, 2019-03-29T22:15:54, 4567.12");
        assertEquals(list.size(), 3);
    }
}