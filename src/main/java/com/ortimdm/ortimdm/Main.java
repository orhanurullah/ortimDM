package com.ortimdm.ortimdm;

import com.ortimdm.ortimdm.service.DownloadRequiredApps;
import com.ortimdm.ortimdm.service.LicenceService;
import com.ortimdm.ortimdm.service.LoggerService;
import com.ortimdm.ortimdm.service.ProjectSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class Main extends Application {

    private static final ObservableList<Stage> stages = FXCollections.observableArrayList();

    private final LicenceService licenseService;

    public Main(){
        this.licenseService = new LicenceService();
    }

    @Override
    public void init() throws Exception {
        System.out.println("Program başlıyor...");
        DownloadRequiredApps dra = new DownloadRequiredApps();
        ProjectSettings projectSettings = new ProjectSettings();
        projectSettings.createRequiredFolders("Downloads", "Ortim");
        projectSettings.createRequiredFolders("Downloads/Ortim", "Videos");
        projectSettings.createRequiredFolders("Downloads/Ortim", "Files");
        Thread thread = new Thread(dra::downloadYtDlp);
        thread.start();
        Thread thread1 = new Thread(dra::downloadFfmpeg);
        thread1.start();
    }

    @Override
    public void start(Stage stage) throws IOException {
        authentication();
        Properties properties = properties();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle(properties.getProperty("app.title"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(icon());
        stage.show();

    }
    @Override
    public void stop() throws Exception {
        System.out.println("Program sonlanıyor.");
        LoggerService.getLogMessages().append("Program sonlanıyor...");
        Platform.exit();
    }

    private Properties properties() throws IOException {
        Properties properties = new Properties();
        try(InputStream inputStream = getClass().getResourceAsStream("/application.properties")){
            properties.load(inputStream);
        }
        return properties;
    }

    private void addProperties(String key, String value) throws IOException {
        Properties properties = properties();
        String userDir = System.getProperty("user.dir");
        String projectDir = userDir + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "application.properties";
        System.out.println(projectDir);
        File file = new File(projectDir);
        if(!file.exists()){
            Files.createFile(file.toPath());
        }
        try(OutputStream outputStream = new FileOutputStream(file)){
            properties.put(key, value);
            properties.store(outputStream, null);
        }
    }

    private boolean controlLicence() throws IOException {
        String key = properties().getProperty("app.license.key");
        System.out.println("control license => " + key);
        System.out.println(licenseService.validateLicenseKey(key));
        return licenseService.validateLicenseKey(key);
    }
    private void authentication() throws IOException {
        if(!controlLicence()){
            LoggerService.addLogMessage("Licence key is not valid");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Enter your licence key");
            alert.setTitle(properties().getProperty("app.authentication.alert.title", "LICENCE"));
            ImageView imageView = new ImageView(icon());
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            alert.setGraphic(imageView);
            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);

            Label label = new Label("Licence Key:");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            TextField textField = new TextField();
            textField.setPromptText("Enter your licence key");
            textField.setStyle("-fx-pref-width: 250px; -fx-pref-height: 30px; -fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

            Hyperlink link = new Hyperlink("Take a licence key");
            String baseUrl = properties().getProperty("base.url");
            System.out.println(baseUrl);
            link.setOnAction(e -> {
                getHostServices().showDocument(baseUrl);
            });
            link.setTextFill(Color.PURPLE);
            link.setCursor(Cursor.HAND);
            vBox.getChildren().addAll(label, textField, link);

            alert.getDialogPane().setContent(vBox);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(response -> {
                if (response == okButton) {
                    try {
                        createLicenceFile(textField.getText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Licence key addition process cancelled.");
                    Platform.exit();
                }
            });
        }
    }

    private Image icon(){
        return new Image(String.valueOf(getClass().getResource("/logo.png")));
    }

    private void createLicenceFile(String text) throws IOException {
        String licenseKey = licenseService.generateLicenseKey();
        System.out.println("Lisans key => " + licenseKey);
        licenseService.saveLicenseKey(licenseKey);
        addProperties("app.data.license.key", text);
        System.out.println("License key => " + text);
        addProperties("app.license.key", licenseKey);
        System.out.println("app.license.key => " + licenseKey);
        properties().setProperty("app.licence.key", licenseKey);
        System.out.println(properties().getProperty("app.licence.key"));
        if(controlLicence()){

        }
    }

    public static void main(String[] args) {
        launch();
    }
}