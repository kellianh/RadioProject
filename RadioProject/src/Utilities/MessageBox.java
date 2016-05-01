package utilities;

/**
 * Created by Brayden on 5/1/2016.
 */
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author Brayden
 * based off of http://code.makery.ch/blog/javafx-dialogs-official/
 */
public class MessageBox
{
    public static void ShowInformation(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void ShowWarning(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void ShowError(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static boolean ShowConfirm(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        boolean confirmed = false;

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            confirmed = true;
        } else {
            confirmed = false;
        }
        return confirmed;
    }

    public static String ShowTextInputBox(String title, String header, String content, String textInput)
    {
        TextInputDialog dialog = new TextInputDialog(textInput);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        else
            return null;
    }

    public static String ShowChoiceTextBox(String title, String header, String content, List<String> choices)
    {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        else
            return null;
    }

}
