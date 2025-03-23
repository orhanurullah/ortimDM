package com.ortimdm.ortimdm.controller;


import com.ortimdm.ortimdm.service.DownloadVideo;
import com.ortimdm.ortimdm.service.LoggerService;
;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

import java.util.concurrent.ExecutionException;


public class HomeController {

    @FXML
    public HBox footer;

    @FXML
    public Label statusLabel;

    @FXML
    public AnchorPane infoAnchorPane;

    @FXML
    public VBox leftBar;

    @FXML
    public ScrollPane listScroll;

    @FXML
    private TextField urlTextField;

    @FXML
    private Button analyzeButton;

    DownloadVideo downloadVideo;

    public HomeController(){
        System.out.println("Uygulama başlıyor...");
        downloadVideo = new DownloadVideo();
    }


    private void analyze() throws IOException{
        String url = this.urlTextField.getText();
        System.out.println("Home controller analyze() methodu");
        if(url == null || url.isEmpty() || !this.urlTextField.getText().startsWith("http")){
            statusLabel.setText("Url alanı boş kalamaz ve içine bir link girmeli...");
            LoggerService.addLogMessage("Link girmelisin...");
            return;
        }
        LoggerService.addLogMessage("Analiz işlemi başlıyor...");
        LoggerService loggerService = new LoggerService();
        VBox vBox = loggerService.showLogs();
        infoAnchorPane.getChildren().add(vBox);
        PopupController popupController = new PopupController();
        downloadVideo.setUrl(url);
        System.out.println("Url => " + url);
        popupController.openSelectorPopup();
    }

    public void doAnalyze() throws IOException, ExecutionException, InterruptedException {
        analyze();
    }
    public void download(String foldername, String format) throws IOException {
        System.out.println("status label" + statusLabel.getText());
        LoggerService.addLogMessage(urlTextField.getText() + " linkinden indirme talebi...");
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