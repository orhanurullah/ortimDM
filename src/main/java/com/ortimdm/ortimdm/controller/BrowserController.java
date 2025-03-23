package com.ortimdm.ortimdm.controller;

import com.ortimdm.ortimdm.Main;
import com.ortimdm.ortimdm.service.LoggerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class BrowserController {

    private static final String defaultUrl = "https://google.com";

    @FXML
    private TextField urlField;

    @FXML
    private Button options;

    @FXML
    private WebView webView;

    private static Stage stage;

    @FXML
    public void openBrowserAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("browser.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        if(stage == null){
            stage = new Stage();
        }
        stage.setTitle("Ortim Browser");
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/logo.png"))));
        stage.show();
        stage.setOnCloseRequest(event -> {
            LoggerService.addLogMessage("Browser kapanıyor...");
            stage.close();
        });
    }

    public BrowserController(){
        System.out.println("Browser başlayacak...");
        LoggerService.addLogMessage("Browser başlıyor...");
    }

    public void getOptions(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Sayfayla ilgi seçenekler araştırılıyor.");
        alert.show();
    }

    public void goToUrl() {
        String url = urlField.getText();
        if (!url.isEmpty()) {
            callWebViewEngine(url);
        }else{
            callWebViewEngine(defaultUrl);
        }
    }

    public void callWebViewEngine(String url){
        WebEngine engine = webView.getEngine();
        if (!url.isEmpty()) {
            engine.load(url);
        }else{
            engine.load(defaultUrl);
        }
    }
}
