package au.com.touchcorp.afterpay.cc.fraud.service;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DetectFraudService {

    Set<String> detectFraud(List<CCTransactionRecord> transactions, Date date, double threshold);

}
