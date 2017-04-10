package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import date_printer.DatesPrinter;

public class SimpleDateFormatTest {

   private static final String                        DATE_FORMAT = "yyyy-MM-dd";
   private static final SimpleDateFormat              unsafe      = new SimpleDateFormat(DATE_FORMAT);
   private static final ThreadLocal<SimpleDateFormat> safe        = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT));

   private List<Date>                                 testDates;
   private Map<String, Integer>                       expected;

   @Before
   public void setUp() {
      // prepare dates and expected results
      SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
      Calendar cal = Calendar.getInstance();
      this.testDates = new ArrayList<>(100);
      this.expected = new HashMap<>(100);
      for (int i = 0; i < 100; i++) {
         cal.add(Calendar.DAY_OF_YEAR, 1);
         this.testDates.add(cal.getTime());
         this.expected.put(format.format(cal.getTime()), 0);
      }
   }

   @Test
   public void testThreadSafe() {
      // print dates
      List<String> strings = DatesPrinter.printDates(this.testDates, () -> safe.get());

      // count strings occurences
      for (String s : strings) {
         if (this.expected.containsKey(s)) {
            this.expected.put(s, this.expected.get(s) + 1);
         } else {
            fail("Found not expected date: " + s);
         }
      }

      // check if every date is present only once
      assertTrue(checkExpected());
   }

   @Test
   public void testThreadUnsafe() {
      // print dates
      List<String> strings = DatesPrinter.printDates(this.testDates, () -> unsafe);

      // count strings occurences
      for (String s : strings) {
         if (this.expected.containsKey(s)) {
            this.expected.put(s, this.expected.get(s) + 1);
         } else {
            fail("Did not expect a value " + s);
         }
      }

      // check if every date is present only once
      assertTrue(checkExpected());
   }

   private boolean checkExpected() {
      boolean ret = true;
      for (Integer val : this.expected.values()) {
         ret = ret && (val == 1);
      }
      return ret;
   }
}
