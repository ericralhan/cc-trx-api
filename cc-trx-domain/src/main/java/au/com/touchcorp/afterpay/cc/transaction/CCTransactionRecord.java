package au.com.touchcorp.afterpay.cc.transaction;

import java.util.Date;

public class CCTransactionRecord implements Comparable<CCTransactionRecord> {

    private String ccHashNum;
    private Date trxDate;
    private double trxAmount;

    public String getCcHashNum() {
        return ccHashNum;
    }

    public void setCcHashNum(String ccHashNum) {
        this.ccHashNum = ccHashNum;
    }

    public Date getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(Date trxDate) {
        this.trxDate = trxDate;
    }

    public double getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(double trxAmount) {
        this.trxAmount = trxAmount;
    }

    public int compareTo(CCTransactionRecord o) {

        return this.ccHashNum.compareTo(o.ccHashNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CCTransactionRecord)) return false;

        CCTransactionRecord that = (CCTransactionRecord) o;

        if (Double.compare(that.getTrxAmount(), getTrxAmount()) != 0) return false;
        if (!getCcHashNum().equals(that.getCcHashNum())) return false;
        return getTrxDate().equals(that.getTrxDate());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getCcHashNum().hashCode();
        result = 31 * result + getTrxDate().hashCode();
        temp = Double.doubleToLongBits(getTrxAmount());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CCTransactionRecord{" +
                "ccHashNum='" + ccHashNum + '\'' +
                ", trxDate=" + trxDate +
                ", trxAmount=" + trxAmount +
                '}';
    }

}
