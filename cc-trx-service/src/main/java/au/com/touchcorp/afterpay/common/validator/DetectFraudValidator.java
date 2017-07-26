package au.com.touchcorp.afterpay.common.validator;

import au.com.touchcorp.afterpay.cc.request.DetectFraudRequest;
import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import au.com.touchcorp.afterpay.common.exception.CCTransactionValidationException;

import java.util.List;

public interface DetectFraudValidator {

    List<CCTransactionRecord> validateDetectFraudRequest(DetectFraudRequest detectFraudRequest) throws CCTransactionValidationException;
}
