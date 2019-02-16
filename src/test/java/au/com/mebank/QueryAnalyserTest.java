package au.com.mebank;

import au.com.mebank.model.RelativeAccount;
import au.com.mebank.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class QueryAnalyserTest {

    private QueryAnalyser queryAnalyser;

    private List<Transaction> transactions = ReversalScrubber.getScrubbedTransactions(Arrays.stream(new String[] {
            "TX10001, ACC334455, ACC778899, 20/10/2018 12:47:55, 25.00, PAYMENT",
            "TX10003, ACC998877, ACC778899, 20/10/2018 18:00:00, 5.00, PAYMENT",
            "TX10005, ACC334455, ACC778899, 21/10/2018 09:30:00, 7.25, PAYMENT"
    }));

    @Before
    public void setup() {
        queryAnalyser = new QueryAnalyser(transactions);
    }

    @Test
    public void testQueryAnalyserPostiveRelativeAccount() throws ParseException {
        RelativeAccount output = queryAnalyser.analyse("ACC778899",
                "20/10/2018 12:47:54", "21/10/2018 09:30:01");
        assertEquals(3, output.getCount());
        assertEquals(new BigDecimal(37.25), output.getAmount());
    }

    @Test
    public void testQueryAnalyserNegativeRelativeAccount() throws ParseException {
        RelativeAccount output = queryAnalyser.analyse("ACC334455",
                "20/10/2018 12:47:54", "21/10/2018 09:30:01");
        assertEquals(2, output.getCount());
        assertEquals(new BigDecimal(-32.25), output.getAmount());
    }

    @Test
    public void testQueryAnalyserOnTheTimeBoundary() throws ParseException {
        RelativeAccount output = queryAnalyser.analyse("ACC778899",
                "20/10/2018 12:47:55", "21/10/2018 09:30:00");
        assertEquals(3, output.getCount());
        assertEquals(new BigDecimal("37.25"), output.getAmount());
    }

    @Test
    public void testQueryAnalyserInTheTimeBoundary() throws ParseException {
        RelativeAccount output = queryAnalyser.analyse("ACC778899",
                "20/10/2018 12:47:54", "21/10/2018 09:30:01");
        assertEquals(3, output.getCount());
        assertEquals(new BigDecimal("37.25"), output.getAmount());
    }
}
