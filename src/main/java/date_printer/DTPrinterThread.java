package date_printer;

import java.util.Date;

public class DTPrinterThread implements Runnable {

   private final Date       date;
   private final SDFAdapter formatAdapter;
   private String           formattedDate = null;

   public DTPrinterThread(Date date, SDFAdapter formatAdapter) {
      this.date = date;
      this.formatAdapter = formatAdapter;
   }

   @Override
   public void run() {
      this.formattedDate = this.formatAdapter.getDateFormat().format(this.date);
   }

   public String getResult() {
      return this.formattedDate;
   }
}
