package au.com.touchcorp.afterpay.common.exception;

public class CCTransactionPartialDataException extends CCTransactionParserException {

    public CCTransactionPartialDataException(String msg) {
        super(msg);
    }
}
