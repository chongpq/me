package au.com.mebank;

import au.com.mebank.model.Transaction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ReversalScrubberTest {

    @Test
    public void testGetScrubbedTransactions() {
        String[] strings = new String[] {
                "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
                "TX10002, ACC334455, ACC998877, 20/10/2018 17:33:43, 10.50, PAYMENT",
                "TX10003, ACC998877, ACC778899, 20/10/2018 18:00:00, 5.00, PAYMENT",
                "TX10004, ACC334455, ACC998877, 20/10/2018 19:45:00, 10.50, REVERSAL, TX10002",
                "TX10005, ACC334455, ACC778899, 21/10/2018 09:30:00, 7.25, PAYMENT"
        };
        List<Transaction> output = ReversalScrubber.getScrubbedTransactions(Arrays.stream(strings));
        assertEquals(3, output.size());
        List<String> comparisonOutput = output.stream()
                .map(Transaction::getTransactionId)
                .collect(Collectors.toList());
        assertTrue(comparisonOutput.contains("TX10001"));
        assertTrue(comparisonOutput.contains("TX10003"));
        assertTrue(comparisonOutput.contains("TX10005"));
    }

    @Test
    public void testGetScrubbedTransactionsEmptyResult() {
        String[] strings = new String[] {
                "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
                "TX10002, ACC334455, ACC998877, 20/10/2018 17:33:43, 10.50, PAYMENT",
                "TX10004, ACC334455, ACC998877, 20/10/2018 19:45:00, 10.50, REVERSAL, TX10002",
                "TX10005, ACC334455, ACC778899, 21/10/2018 09:30:00, 25.00, REVERSAL, TX10001"
        };
        List<Transaction> output = ReversalScrubber.getScrubbedTransactions(Arrays.stream(strings));
        assertTrue(output.isEmpty());
    }

    @Test
    public void testGetScrubbedTransactionsReversalTransactionDoesntExist() {
        String[] strings = new String[] {
                "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
                "TX10002, ACC334455, ACC998877, 20/10/2018 17:33:43, 10.50, PAYMENT",
                "TX10004, ACC334455, ACC998877, 20/10/2018 19:45:00, 10.50, REVERSAL, TX10003",
                "TX10005, ACC334455, ACC778899, 21/10/2018 09:30:00, 25.00, REVERSAL, TX10001"
        };
        List<Transaction> output = ReversalScrubber.getScrubbedTransactions(Arrays.stream(strings));
        assertEquals(1, output.size());
        assertEquals("TX10002", output.get(0).getTransactionId());
    }

    @Test
    public void testGetScrubbedTransactionsReversalIncorrectDetails() {
        String[] strings = new String[] {
                "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
                "TX10005, ACC998877, ACC778899, 21/10/2018 09:30:00, 5.00, REVERSAL, TX10001"
        };
        List<Transaction> output = ReversalScrubber.getScrubbedTransactions(Arrays.stream(strings));
        assertTrue(output.isEmpty());
    }

    @Test
    public void testGetScrubbedTransactionsReversalRemoveManyTransacitons() {
        String[] strings = new String[] {
                "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
                "TX10001, ACC334455, ACC998877, 20/10/2018 17:33:43, 10.50, PAYMENT",
                "TX10005, ACC998877, ACC778899, 21/10/2018 09:30:00, 5.00, REVERSAL, TX10001"
        };
        List<Transaction> output = ReversalScrubber.getScrubbedTransactions(Arrays.stream(strings));
        assertTrue(output.isEmpty());
    }
}
