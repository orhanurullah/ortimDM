package com.ortimdm.ortimdm.controller;

import com.ortimdm.ortimdm.Main;
import com.ortimdm.ortimdm.service.DownloadVideo;
import com.ortimdm.ortimdm.service.LoggerService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class PopupController implements Initializable{

    @FXML
    public Button downloadButton;
    @FXML
    public TextField folderName;
    @FXML
    public ComboBox<String> formatSelector;
    @FXML
    public Label fileInfoLabel;
    @FXML
    public Label formatSelectorLabel;

    @FXML
    public Label fileTypeLabel;
    @FXML
    public ComboBox<String> fileTypeSelector;

    Tooltip tooltip = new Tooltip();

    private static Stage stage;

    public static String thumbnail;
    public static String fileName;
    public static String fileFormat;

    private final String[] fileTypes = {"Video", "Audio"};
    private final String[] audioFormats = {"MP3", "AAC", "ALAC", "FLAC", "M4A", "WAV", "OPUS", "VARBIS"};
    private final String[] videoFormats = {"MP4", "AVI", "MKV", "WEBM", "MOV", "FLV"};

    public PopupController(){
        this.fileInfoLabel = new Label();
        if(fileName != null){
            fileInfoLabel.setText(fileName + "." + fileFormat);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        info();
        fileTypeSelector.getItems().addAll(Arrays.stream(fileTypes).toList());
        fileTypeSelector.setValue(fileTypes[0]);
        formatSelector.getItems().addAll(Arrays.stream(videoFormats).toList());
        formatSelector.setValue(videoFormats[0]);
    }

    @FXML
    public void openSelectorPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("popup.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        if(stage == null){
            stage = new Stage();
        }
        stage.setResizable(false);
        stage.setTitle("İndirme Seçenekleri");
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/logo.png"))));
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Çıkış");
            alert.setHeaderText("Çıkış işlemi yapılsın mı?");
            ButtonType result = alert.showAndWait().get();
            if(result == ButtonType.OK){
                stage.close();
            }else{
                event.consume();
            }
        });
//        stage.setX(100);
//        stage.setY(0);
        stage.show();
    }

    public void download() throws IOException {
        System.out.println("Download Popup");
        MainController mainController = new MainController();
        String folderName = this.folderName.getText();
        if(folderName == null || folderName.equals(" ")){
            folderName = " ";
        }
        mainController.download(folderName, this.selectFormat());
        Stage stage = (Stage) downloadButton.getScene().getWindow();
        stage.close();

    }

    private void updateFormatSelector(String fileType){
        ObservableList<String> items = FXCollections.observableArrayList();
        if(fileType.equals(fileTypes[0])){
            items.addAll(Arrays.stream(videoFormats).toList());
            formatSelectorLabel.setText("Video formatını seçin. Varsayılan mp4");
        }else if(fileType.equals(fileTypes[1])){
            items.addAll(Arrays.stream(audioFormats).toList());
            formatSelectorLabel.setText("Ses dosyası formatını seçin. Varsayılan mp3");
        }
        formatSelector.setItems(items);
        formatSelector.setValue(items.get(0));
    }

    public String selectFormat(){
        String selectedFormat = formatSelector.getValue();
        if (selectedFormat != null) {
           return selectedFormat.toLowerCase();
        } else {
            return " ";
        }
    }

    public void info(){
        LoggerService.addLogMessage("Link araştırılıyor...");
       DownloadVideo downloadVideo = new DownloadVideo();
       String url = downloadVideo.getUrl();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println(url);
                String[] command = {"yt-dlp", "--get-filename", "--get-thumbnail", "--simulate" , "--encoding", "UTF-8", url};

                Process process = Runtime.getRuntime().exec(command);
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader inputReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = inputReader.readLine()) != null) {
                    if(line.startsWith("http")){
                        thumbnail = line;
                        LoggerService.addLogMessage("Dosyanın ön izleme resim url adresi="  + thumbnail);
                    }
                    line = line.replaceAll("\\[.*?]", "");
                    String[] parts = line.split("\\.");

                    fileName = parts[0].trim().toLowerCase(Locale.ROOT);
                    fileFormat = parts[1].trim().toLowerCase(Locale.US);
                    Platform.runLater(() -> {
                        System.out.println(parts[0]);
                        System.out.println(parts[1]);
                        System.out.println(thumbnail);
                        fileInfoLabel.setText(fileName + "." + fileFormat);
                        tooltip.setText(fileName + "." + fileFormat);
                        fileInfoLabel.setTooltip(tooltip);
                        LoggerService.addLogMessage(fileName + "." + fileFormat + " dosyası bulundu.");
                    });
                }
                int exitCode = process.waitFor();
                System.out.println("Çıkış kodu: " + exitCode);
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            System.out.println("İşlem tamam");
            LoggerService.addLogMessage("Link inceleme işlemi tamamlandı.");
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void selectFileType(){
        updateFormatSelector(this.fileTypeSelector.getValue());
    }
}
