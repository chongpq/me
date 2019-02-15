package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class QueryAnalyser {

    private final List<Transaction> transactions;
    private final SimpleDateFormat sdf;

    QueryAnalyser(List<Transaction> transactions) {
        this.transactions = transactions;
        this.sdf = new SimpleDateFormat(Transaction.DATE_PATTERN);
    }

    RelativeAccount analyse(String accountId, String from, String to) throws ParseException {
        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);
        return transactions.stream()
                .filter(transaction -> transaction.getCreatedAt().before(toDate)
                        && transaction.getCreatedAt().after(fromDate))
                .filter(transaction -> transaction.getFromAccountId().equals(accountId)
                        || transaction.getToAccountId().equals(accountId))
                .reduce(new RelativeAccount(accountId, 0, BigDecimal.ZERO),
                        RelativeAccount::addTransaction, RelativeAccount::add);
    }
}
