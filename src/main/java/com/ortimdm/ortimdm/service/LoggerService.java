package com.ortimdm.ortimdm.service;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


public class LoggerService {

    private static final StringBuilder logMessages = new StringBuilder();

    private final Label label;

    private final ScrollPane scrollPane;
    private final VBox vBox;
    private final Button downloadButton;

    public LoggerService(){
        this.label = new Label();
        this.scrollPane = new ScrollPane();
        label.setPadding(new Insets(10));
        scrollPane.setPrefWidth(550);
        scrollPane.setPrefHeight(300);
        scrollPane.setPadding(new Insets(20));
        this.vBox = new VBox();
        this.downloadButton = new Button("Download");
        this.downloadButton.setStyle("-fx-padding: 10; -fx-spacing: 5; -fx-border-color: green; -fx-alignment: center;");
        vBox.getChildren().addAll(scrollPane, downloadButton);
    }

    public static void addLogMessage(String message) {
        logMessages.append(message).append("\n");
    }

    public VBox showLogs(){
        startLogUpdaterThread();
        return this.vBox;
    }

    private void updateLogLabel() {
        Platform.runLater(() -> {
            label.setText(logMessages.toString());
            scrollPane.setContent(label);
            scrollPane.setVvalue(1.0);
        });
    }

    private void startLogUpdaterThread() {
        Thread updaterThread = new Thread(() -> {
            while (true) {
                // Log mesajlarını güncelle
                updateLogLabel();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updaterThread.setDaemon(true);
        updaterThread.start();
    }

    public static StringBuilder getLogMessages() {
        return logMessages;
    }
}
