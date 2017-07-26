package au.com.touchcorp.afterpay.cc.fraud.service;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetectFraudServiceImpl implements DetectFraudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetectFraudServiceImpl.class);

    @Override
    public Set<String> detectFraud(List<CCTransactionRecord> transactions, Date fraudDate, double threshold) {

        LOGGER.trace("detectFraud start");

        List<CCTransactionRecord> singleDayTransactions = findSingleDayTransactions(transactions, fraudDate);

        Map<String, Double> clubbedCCTrxMap = groupTransactionsByCreditCard(singleDayTransactions);

        Map<String, Double> fraudCCMap = findFraudCreditCards(clubbedCCTrxMap, threshold);

        LOGGER.trace("detectFraud end");

        return fraudCCMap.keySet();
    }

    private List<CCTransactionRecord> findSingleDayTransactions(List<CCTransactionRecord> transactions, Date fraudDate) {
        LOGGER.trace(transactions.size() + " transactions received for processing");

        // filter list for fraudDate
        List<CCTransactionRecord> singleDayTransactions = transactions.stream().filter(transaction ->
                DateUtils.isSameDay(transaction.getTrxDate(), fraudDate))
                .collect(Collectors.toList());

        LOGGER.trace(singleDayTransactions.size() + " transactions took place on date " + fraudDate);
        return singleDayTransactions;
    }

    private Map<String, Double> groupTransactionsByCreditCard(List<CCTransactionRecord> singleDayTransactions) {

        // this map will have unique ccNums with total trxAmount for each day
        Map<String, Double> clubbedCCTrxMap = new HashMap<>();

        singleDayTransactions.forEach(singleDayTransactionRecord -> {

            String ccHashNum = singleDayTransactionRecord.getCcHashNum();
            double trxAmount = singleDayTransactionRecord.getTrxAmount();

            if (!clubbedCCTrxMap.containsKey(ccHashNum)) {

                clubbedCCTrxMap.put(ccHashNum, trxAmount);

            } else {

                clubbedCCTrxMap.put(ccHashNum, (clubbedCCTrxMap.get(ccHashNum)) + trxAmount);
            }
        });

        clubbedCCTrxMap.forEach((key, value) -> LOGGER.trace(key + " credit card spent " + value + "on that day"));

        return clubbedCCTrxMap;
    }

    private Map<String, Double> findFraudCreditCards(Map<String, Double> clubbedCCTrxMap, double threshold) {

        // filter for fraud CCNums
        Map<String, Double> fraudCCMap = clubbedCCTrxMap.entrySet().stream()
                .filter(record -> (record.getValue() > threshold))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        fraudCCMap.entrySet().forEach(fraudRecord -> LOGGER.trace("Fraud Detected on CC " + fraudRecord));

        return fraudCCMap;
    }

}
