package mbogdanos.ezglossa;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mbogdanos.ezglossa.files.FileHandlingUtils;
import mbogdanos.ezglossa.utils.SavePromptButtonResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainController {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class);

    @FXML
    private TextArea textArea;
    @FXML
    private Stage stage;

    private File loadedFileReference;
    private FileChooser fileChooser;

    public void initialize() {
        fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser
                .getExtensionFilters()
                .addAll(
                        new FileChooser.ExtensionFilter("Text", "*.txt"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
    }


    @FXML
    public void newFile() {

        SavePromptButtonResult saveButtonResult = showSavePrompt();

        switch (saveButtonResult) {
            case NO -> {
                textArea.clear();
                textArea.setEditable(true);
            }
            case YES -> {
                saveFile();
                textArea.clear();
                textArea.setEditable(true);
            }
        }
    }


    @FXML
    protected void openFile() {

        SavePromptButtonResult saveButtonResult = showSavePrompt();

        switch (saveButtonResult) {
            case CANCEL:
                return;
            case YES:
                saveFile();
                break;
        }

        File fileToLoad = fileChooser.showOpenDialog(null);
        if (fileToLoad == null) {
            LOGGER.debug("Null file returned, no choice from user.");
            return;
        }

        Task<String> fileLoaderTask = FileHandlingUtils.generateFileLoaderTask(fileToLoad);
        fileLoaderTask.setOnSucceeded(workerStateEvent -> {
            try {
                this.textArea.setText(fileLoaderTask.get());
                this.loadedFileReference = fileToLoad;
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e);
                this.textArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
                Thread.currentThread().interrupt();
            }
        });

        fileLoaderTask.setOnFailed(workerStateEvent ->
                this.textArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath()));

        fileLoaderTask.run();
        textArea.setEditable(true);
    }


    @FXML
    public void saveFile() {
        try {
            if (loadedFileReference == null) {
                loadedFileReference = fileChooser.showSaveDialog(stage);
            }

            if (loadedFileReference == null) {
                LOGGER.debug("User did not choose a file to save to.");
                return;
            }

            try (FileWriter myWriter = new FileWriter(loadedFileReference)) {
                myWriter.write(textArea.getText());
            }

            LOGGER.debug("Successfully wrote to the file.");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @FXML
    public void exit() {
        if (textArea.getText().isEmpty()) {
            Platform.exit();
            return;
        }

        SavePromptButtonResult saveButtonResult = showSavePrompt();

        switch (saveButtonResult) {
            case NO -> Platform.exit();
            case YES -> {
                saveFile();
                Platform.exit();
            }
        }
    }

    private SavePromptButtonResult showSavePrompt() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Do you want to save the current file?",
                ButtonType.YES,
                ButtonType.NO,
                ButtonType.CANCEL
        );

        alert.showAndWait();

        SavePromptButtonResult saveButtonResult = SavePromptButtonResult.fromButtonType(alert.getResult());
        Objects.requireNonNull(saveButtonResult, "No mapping for button " + alert.getResult());

        return saveButtonResult;
    }
}