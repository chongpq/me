package au.com.mebank;

import au.com.mebank.model.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReversalScrubber {

    private static final String CSV_SPLIT_REGEX = "\\s*,\\s*";

    public static List<Transaction> getScrubbedTransactions(Stream<String> stream) {
        Set<String> removeTransactions = new HashSet<>();
        List<Transaction> transactions = new ArrayList<>();

        stream.forEachOrdered(line -> {
            String[] strings = line.split(CSV_SPLIT_REGEX);
            if (strings.length <= 6) {
                transactions.add(new Transaction(strings[0], strings[1], strings[2],
                        strings[3], strings[4]));
            } else {
                removeTransactions.add(strings[6]);
            }
        });

        return transactions.stream()
                .filter(transaction -> !removeTransactions.contains(transaction.getTransactionId()))
                .collect(Collectors.toList());
    }
}
