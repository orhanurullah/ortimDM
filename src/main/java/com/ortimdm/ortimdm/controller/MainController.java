package com.ortimdm.ortimdm.controller;

import com.ortimdm.ortimdm.Main;
import com.ortimdm.ortimdm.service.DownloadVideo;
import com.ortimdm.ortimdm.service.LoggerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class MainController {

    @FXML
    public HBox footer;

    @FXML
    public Label statusLabel;

    @FXML
    public SplitPane split;

    @FXML
    public AnchorPane infoAnchorPane;
    @FXML
    public VBox leftbar;
    @FXML
    public ImageView infoThumbnailView;
    @FXML
    public VBox fileInfoLabel;
    @FXML
    public Label resultAnalyzeTitle;
    @FXML
    public Label infoWillDownloadFile;
    @FXML
    public Button downloadButton;

    @FXML
    private TextField uri;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem openBrowser;

    @FXML
    private Button analyzeButton;

    static DownloadVideo downloadVideo;

    public TextField getUri() {
        return uri;
    }

    public MainController(){
        System.out.println("Uygulama başlıyor...");
        statusLabel = new Label();
        uri = new TextField();
        downloadVideo = new DownloadVideo();
    }

    private void analyze() throws IOException {
        String url = this.uri.getText();
        if(url == null || url.isEmpty()){
            statusLabel.setText("Url alanını boş bırakmayın...");
            LoggerService.addLogMessage("Link girmelisin...");
            return;
        }
        LoggerService.addLogMessage("Analiz işlemi başlıyor...");
        LoggerService loggerService = new LoggerService();
        VBox scrollPane = loggerService.showLogs();
        infoAnchorPane.getChildren().add(scrollPane);

        PopupController popupController = new PopupController();
        downloadVideo.setUrl(uri.getText());
        System.out.println("Url => " + uri.getText());
        popupController.openSelectorPopup();
    }

    public void doAnalyze() throws IOException {
        analyze();
    }
    public void download(String foldername, String format) throws IOException {
        System.out.println("status label" + statusLabel.getText());
        LoggerService.addLogMessage(uri.getText() + " linkinden indirme talebi...");
        if(!foldername.equals(" ")){
            downloadVideo.setFolder(foldername);
        }
        if(!format.equals(" ")){
            downloadVideo.setFormat(format);
        }
        statusLabel.textProperty().bind(downloadVideo.messageProperty());
        downloadVideo.setOnSucceeded(e -> {
            if (downloadVideo.getValue()) {
                statusLabel.textProperty().bind(downloadVideo.messageProperty());
                LoggerService.addLogMessage("İndirme tamamlandı.");
                System.out.println("İndirme tamamlandı! " + downloadVideo.messageProperty());
            } else {
                statusLabel.textProperty().bind(downloadVideo.messageProperty());
                LoggerService.addLogMessage("DİKKAT! İndirme gerçeleşemedi.");
                System.out.println("İndirme hatası! " + downloadVideo.messageProperty());
            }
        });
        downloadVideo.setOnFailed(e -> {
            statusLabel.textProperty().bind(downloadVideo.messageProperty());
            LoggerService.addLogMessage("DİKKAT! Başarısız işlem");
            System.out.println("İndirme başarısız: " + e.getSource());
        });
        downloadVideo.setOnRunning(e -> {
            statusLabel.textProperty().bind(downloadVideo.messageProperty());
            LoggerService.addLogMessage("İndirme işlemi başladı...");
            System.out.println("İndirme başladı...");
        });
        new Thread(downloadVideo).start();
    }

    public void openFileAction(){
        System.out.println("Open file menüsü tıklandı");
    }
    public void openBrowserAction() throws IOException {
        System.out.println("Open browser menüsü tıklandı");
        BrowserController browserController = new BrowserController();
        browserController.openBrowserAction();
    }
}