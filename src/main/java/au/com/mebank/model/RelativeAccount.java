package au.com.mebank.model;

import java.math.BigDecimal;

public class RelativeAccount {

    private final int count;
    private final BigDecimal amount;
    private final String accountId;

    public int getCount() {
        return count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RelativeAccount(String accountId, int count, BigDecimal amount) {
        this.accountId = accountId;
        this.count = count;
        this.amount = amount;
    }

    public RelativeAccount addTransaction(Transaction transaction) {
        if (!transaction.getFromAccountId().equals(transaction.getToAccountId())) {
            if (accountId.equals(transaction.getFromAccountId())) {
                return new RelativeAccount(accountId, count + 1, amount.subtract(transaction.getAmount()));
            } else if (accountId.equals(transaction.getToAccountId())) {
                return new RelativeAccount(accountId, count + 1, amount.add(transaction.getAmount()));
            }
        }

        return this;
    }

    public RelativeAccount add(RelativeAccount relativeAccount) {
        if (accountId.equals(relativeAccount.accountId)) {
            return new RelativeAccount(accountId, count + relativeAccount.getCount(),
                    amount.add(relativeAccount.getAmount()));
        } else {
            return this;
        }
    }
}
