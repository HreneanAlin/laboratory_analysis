package UserInterface;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChoosePath {
    public static String getChosenPath(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        directoryChooser.setTitle(title);
        File prefFile = new File("C:\\Users\\User\\Desktop\\Rezultate");
        if (prefFile.exists()) directoryChooser.setInitialDirectory(prefFile);
        File file = directoryChooser.showDialog(stage);
        return file.getPath();
    }
}
