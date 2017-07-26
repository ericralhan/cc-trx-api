package au.com.touchcorp.afterpay.cc.fraud.service.utils;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionParserException;
import au.com.touchcorp.afterpay.common.exception.CCTransactionPartialDataException;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CCTransactionsParser {

    public static List<CCTransactionRecord> parseTransactions(String commaSeparatedTransactionsString) throws CCTransactionParserException {

        checkEmptyString(commaSeparatedTransactionsString);

        if (!commaSeparatedTransactionsString.contains(",")) {
            throw new CCTransactionPartialDataException("partial data");
        }

        String[] rawTransactionsArray = commaSeparatedTransactionsString.split(",");

        // one transaction record has 3 properties
        if (rawTransactionsArray.length % 3 != 0)
            throw new CCTransactionPartialDataException("partial data");

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss");
        List<CCTransactionRecord> transactions = new ArrayList<>();

        for (int i = 0; i < rawTransactionsArray.length; i += 3) {

            CCTransactionRecord trx = new CCTransactionRecord();

            try {
                trx.setCcHashNum(checkEmptyString(rawTransactionsArray[i]));
                trx.setTrxDate(dtFormat.parse(checkEmptyString(rawTransactionsArray[i + 1])));
                trx.setTrxAmount(Double.parseDouble(checkDecimalFormat(checkEmptyString(rawTransactionsArray[i + 2]))));

            } catch (Exception e) {

                throw new CCTransactionParserException("transaction parsing failed :: " + trx.toString());
            }
            transactions.add(trx);
        }
        return transactions;
    }

    private static String checkEmptyString(String str) throws CCTransactionPartialDataException {

        if (StringUtils.isBlank(str))
            throw new CCTransactionPartialDataException("commaSeparated String is empty");

        return str;
    }

    private static String checkDecimalFormat(String str) throws CCTransactionParserException {

        if (!str.contains("."))
            throw new CCTransactionParserException("not a decimal number: " + str);

        String[] decimalSplit = str.split("\\.");

        // only two decimal places allowed as a valid format
        if (decimalSplit[1].length() != 2)
            throw new CCTransactionParserException("number not in required format: " + str);

        return str;
    }
}
