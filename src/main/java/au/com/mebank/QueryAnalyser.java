package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

class QueryAnalyser {

    private final List<Transaction> transactions;

    QueryAnalyser(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    RelativeAccount analyse(String accountId, Date from, Date to) {
        return transactions.stream()
                .filter(transaction -> transaction.getCreatedAt().before(to)
                        && transaction.getCreatedAt().after(from))
                .filter(transaction -> transaction.getFromAccountId().equals(accountId)
                        || transaction.getToAccountId().equals(accountId))
                .reduce(new RelativeAccount(accountId, 0, BigDecimal.ZERO),
                        RelativeAccount::addTransaction, RelativeAccount::add);
    }
}
