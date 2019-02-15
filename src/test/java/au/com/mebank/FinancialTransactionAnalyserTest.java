package au.com.mebank;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;

import static junit.framework.Assert.assertEquals;

public class FinancialTransactionAnalyserTest {

    @Test
    public void testMainInsufficientParams() throws ParseException {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setErr(ps);
        FinancialTransactionAnalyser.main(new String[] {});
        assertEquals("Insufficient command line arguments\r\n", os.toString());
    }

    @Test
    public void testMainErrorReadingFile() throws ParseException {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setErr(ps);
        FinancialTransactionAnalyser.main(new String[] {"asdf"});
        assertEquals("Error reading file 'asdf'\r\n", os.toString());
    }

}