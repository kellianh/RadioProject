package utilities;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.FileWriter;

/**
 *
 * @author Brayden
 */
public class BasicLogger
{
    static File logDirectory = new File("logs//");

    public static void LogError(String error, String source)
    {

        try
        {
            WriteLine(error + " AT: " + source);
            BufferedWriter log;
            if(!logDirectory.isDirectory())
            {
                logDirectory.mkdir();
            }
            //Build log file name
            Calendar time = GregorianCalendar.getInstance(); //Initilizes to current time
            int month = time.get(Calendar.MONTH);
            int day = time.get(Calendar.DAY_OF_MONTH);
            int year = time.get(Calendar.YEAR);
            String logfileName = month + "-" + day + "-" + year + " log.txt";
            File logPath = new File(logDirectory+"//"+logfileName);
            if(!logPath.exists())
            {
                logPath.createNewFile();
            }
            log = new BufferedWriter(new FileWriter(logPath,true));
            log.append("Error Time: " + time.getTime().toString());
            log.newLine();
            log.append("Error Source: " + source);
            log.newLine();
            log.append("Error Message: ");
            log.newLine();
            log.append(error);
            log.newLine();
            log.newLine();
            log.close();
        }
        catch (Exception ex)
        {
            System.out.println("BasicLogger Error" + ex.getMessage());
        }

    }

    public static void WriteLine(String message)
    {
        System.out.println(message);
    }
}
