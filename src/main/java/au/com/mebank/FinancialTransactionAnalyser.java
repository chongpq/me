package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FinancialTransactionAnalyser {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Insufficient command line arguments");
            return;
        }

        List<Transaction> transactions;
        try (Stream<String> stream = Files.lines(Paths.get(args[0]))) {
            transactions = ReversalScrubber.getScrubbedTransactions(stream);
        } catch (IOException e) {
            System.err.println(String.format("Error reading file '%s'", args[0]));
            return;
        }

        QueryAnalyser queryAnalyser = new QueryAnalyser(transactions);
        SimpleDateFormat sdf = new SimpleDateFormat(Transaction.DATE_PATTERN);

        try(Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("=== press ctrl-c to quit ===");
                System.out.println("Enter account id : ");
                String accountId = scanner.nextLine();
                System.out.println("Enter from date (DD/MM/YYYY hh:mm:ss) : ");
                Date fromDate;
                try {
                    fromDate = sdf.parse(scanner.nextLine());
                } catch (ParseException e) {
                    System.err.println("Incorrect date format. Requires DD/MM/YYYY hh:mm:ss");
                    continue;
                }
                System.out.println("Enter to date (DD/MM/YYYY hh:mm:ss) : ");
                Date toDate;
                try {
                    toDate = sdf.parse(scanner.nextLine());
                } catch (ParseException e) {
                    System.err.println("Incorrect date format. Requires DD/MM/YYYY hh:mm:ss");
                    continue;
                }

                RelativeAccount relativeAccount = queryAnalyser.analyse(accountId, fromDate, toDate);
                System.out.println(String.format("\r\nRelative balance for the period is: %s",
                        relativeAccount.getAmount().toPlainString()));
                System.out.println(String.format("Number of transactions included is: %d",
                        relativeAccount.getCount()));
            }
        }
    }
}
