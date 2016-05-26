package utilities;

import java.util.Arrays;

/**
 * Created by Brayden on 5/1/2016.
 */
public class ErrorHandler
{
    public static void HandleException(Exception ex, String source)
    {
        MessageBox.ShowError("Error!", "There was a program error and a log file was generated: ", ex.getMessage()+ " AT: " + source);
        BasicLogger.LogError(ex.getMessage() + " | " + Arrays.toString(ex.getStackTrace()), source);
    }
}
