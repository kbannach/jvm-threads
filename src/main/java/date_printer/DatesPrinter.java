package date_printer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DatesPrinter {

   /**
    * @return list of formatted {@code dates}
    */
   public static List<String> printDates(List<Date> dates, SDFAdapter formatAdapter) {
      // create tasks list
      List<DTPrinterThread> threads = new ArrayList<>(dates.size());
      dates.stream().forEach(d -> threads.add(new DTPrinterThread(d, formatAdapter)));

      // submit tasks
      List<Future< ? >> futures = new ArrayList<>(threads.size());
      ExecutorService es = Executors.newFixedThreadPool(8);
      threads.stream().forEach(t -> futures.add(es.submit(t)));

      // wait until all tasks finished
      while (futures.stream().allMatch(f -> {
         try {
            return f.get() != null;
         } catch (InterruptedException | ExecutionException | CancellationException e) {
            return true;
         }
      }));
      es.shutdown();

      // collect results
      return threads.stream().map(DTPrinterThread::getResult).collect(Collectors.toList());
   }
}
