package au.com.touchcorp.afterpay.common.exception;

public class CCTransactionValidationException extends CCTransactionApplicationException {
    public CCTransactionValidationException(String msg) {
        super(msg);
    }
}
