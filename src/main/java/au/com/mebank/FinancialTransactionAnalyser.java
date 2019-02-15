package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FinancialTransactionAnalyser {

    public static void main(String[] args) throws ParseException {
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
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("=== press ctrl-c to quit ===");
                System.out.println("Enter account id : ");
                String accountId = scanner.nextLine();
                System.out.println("Enter from date (DD/MM/YYYY hh:mm:ss) : ");
                String from = scanner.nextLine();
                System.out.println("Enter to date (DD/MM/YYYY hh:mm:ss) : ");
                String to = scanner.nextLine();

                RelativeAccount relativeAccount = queryAnalyser.analyse(accountId, from, to);
                System.out.println(String.format("\r\nRelative balance for the period is: %s",
                        relativeAccount.getAmount().toPlainString()));
                System.out.println(String.format("Number of transactions included is: %d",
                        relativeAccount.getCount()));
            }
        } finally {
            scanner.close();
        }
    }
}
