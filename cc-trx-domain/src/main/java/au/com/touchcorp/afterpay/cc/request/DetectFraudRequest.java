package au.com.touchcorp.afterpay.cc.request;

import au.com.touchcorp.afterpay.cc.transaction.CCTransactionRecord;

import java.util.Date;
import java.util.List;

public class DetectFraudRequest {

    private List<CCTransactionRecord> transactions;
    private Date fraudDate;
    private double threshold;

    public List<CCTransactionRecord> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CCTransactionRecord> transactions) {
        this.transactions = transactions;
    }

    public Date getFraudDate() {
        return fraudDate;
    }

    public void setFraudDate(Date fraudDate) {
        this.fraudDate = fraudDate;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
