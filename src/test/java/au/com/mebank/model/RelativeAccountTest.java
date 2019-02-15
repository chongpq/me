package au.com.mebank.model;

import au.com.mebank.ReversalScrubber;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class RelativeAccountTest {

    @Test
    public void testAddTransactionSameAccountIds() {
        RelativeAccount relativeAccount = new RelativeAccount("asdf", 0, BigDecimal.ZERO);
        RelativeAccount output = relativeAccount.addTransaction(ReversalScrubber.getScrubbedTransactions(Arrays.stream(new String[] {
                "TX10005, asdf, asdf, 21/10/2018 09:30:00, 25.00"
        })).get(0));

        assertEquals(0, output.getCount());
        assertEquals(BigDecimal.ZERO, output.getAmount());
    }

    @Test
    public void testAddTransactionCompletelyDifferAccountIds() {
        RelativeAccount relativeAccount = new RelativeAccount("asdf", 0, BigDecimal.ZERO);
        RelativeAccount output = relativeAccount.addTransaction(ReversalScrubber.getScrubbedTransactions(Arrays.stream(new String[] {
                "TX10005, qwer, tyrtu, 21/10/2018 09:30:00, 25.00"
        })).get(0));

        assertEquals(0, output.getCount());
        assertEquals(BigDecimal.ZERO, output.getAmount());
    }

    @Test
    public void testAddCompletelyDifferAccountIds() {
        RelativeAccount relativeAccount = new RelativeAccount("asdf", 2, BigDecimal.ONE);
        RelativeAccount relativeAccount1 = new RelativeAccount("qwer", 3, BigDecimal.ONE);

        RelativeAccount output = relativeAccount.add(relativeAccount1);

        assertEquals(2, output.getCount());
        assertEquals(BigDecimal.ONE, output.getAmount());
    }

    @Test
    public void testAdd() {
        RelativeAccount relativeAccount = new RelativeAccount("asdf", 2, BigDecimal.ONE);
        RelativeAccount relativeAccount1 = new RelativeAccount("asdf", 3, BigDecimal.ONE);

        RelativeAccount output = relativeAccount.add(relativeAccount1);
        assertEquals(5, output.getCount());
        assertEquals(BigDecimal.ONE.add(BigDecimal.ONE), output.getAmount());
    }
}
