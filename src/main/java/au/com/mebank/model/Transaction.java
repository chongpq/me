package au.com.mebank.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    public static final String DATE_PATTERN = "dd/MM/yyyy hh:mm:ss";
    private final String transactionId;
    private final String fromAccountId;
    private final String toAccountId;
    private final Date createdAt;
    private final BigDecimal amount;

    public Transaction(String createdAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            this.createdAt = sdf.parse(createdAt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.transactionId = "";
        this.fromAccountId = "";
        this.toAccountId = "";
        this.amount = BigDecimal.ZERO;
    }

    public Transaction(String transactionId, String fromAccountId,
                       String toAccountId, String createdAt, String amount) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            this.createdAt = sdf.parse(createdAt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat df = new DecimalFormat();
        df.setParseBigDecimal(true);
        try {
            this.amount = (BigDecimal) df.parse(amount);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    BigDecimal getAmount() {
        return amount;
    }
}
