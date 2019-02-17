package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;

class QueryAnalyser {

    public static final Comparator<Transaction> TRANSACTION_COMPARATOR =Comparator.comparing(Transaction::getCreatedAt);

    private final List<Transaction> transactions;

    QueryAnalyser(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    RelativeAccount analyse(String accountId, Date from, Date to) {
        Transaction fromKey = new Transaction(from);
        Transaction toKey = new Transaction(to);
        int fromIndex = getIndex(fromKey);
        int toIndex = min(getIndex(toKey) + 1, transactions.size());
        return transactions.subList(fromIndex, toIndex)
                .stream()
                .filter(transaction -> transaction.getCreatedAt().before(to)
                        && transaction.getCreatedAt().after(from))
                .filter(transaction -> transaction.getFromAccountId().equals(accountId)
                        || transaction.getToAccountId().equals(accountId))
                .reduce(new RelativeAccount(accountId, 0, BigDecimal.ZERO),
                        RelativeAccount::addTransaction, RelativeAccount::add);
    }

    int getIndex(Transaction key) {
        int i = Collections.binarySearch(transactions, key, TRANSACTION_COMPARATOR);
        if (i < 0) {
            return (-1 * i ) - 1;
        } else {
            return i;
        }
    }
}
