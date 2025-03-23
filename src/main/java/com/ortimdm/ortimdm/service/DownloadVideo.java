package com.ortimdm.ortimdm.service;


import javafx.concurrent.Task;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadVideo extends Task<Boolean> {

    private String folder;
    private String format;
    private static String url;

    public DownloadVideo() {
        System.out.println("Download Video Class => ");
    }

    public String getFolder() {
        return folder.toLowerCase();
    }

    public void setFolder(String folder) {
        this.folder = folder.replace(" ", "-");
    }

    public String getFormat() {
        return format.toLowerCase();
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        DownloadVideo.url = url;
    }

    public String download() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String defaultTitleFormat = "%(upload_date)s-%(title)s.%(ext)s";
        stringBuilder.append("yt-dlp -S \"res:1080\" --encoding \"UTF-8\" -o ");
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "/Downloads/Ortim/Videos");
        if(this.folder != null){
            ProjectSettings projectSettings = new ProjectSettings();
            projectSettings.createRequiredFolders("Downloads/Ortim/Videos", this.folder);
            LoggerService.addLogMessage(this.folder + " klasörü oluşturuldu.");
            stringBuilder.append(downloadsPath.resolve(this.getFolder())).append("/").append(defaultTitleFormat);
        }else{
            stringBuilder.append(downloadsPath).append("/").append(defaultTitleFormat);
        }
        if(this.format != null){
            stringBuilder.append(" --format ").append(this.format);
            updateMessage(this.getFormat() + " formatında indiriliyor...");
        }else{
            stringBuilder.append(" --format mp4");
            updateMessage("mp4 formatında indiriliyor...");
        }
        stringBuilder.append(" ").append(this.getUrl());
        LoggerService.addLogMessage("Çalıştırılacak betik hazır : " + stringBuilder);
        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    protected Boolean call() {
        try {
            String command = download();
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = inputReader.readLine()) != null) {
                System.out.println(line);
                LoggerService.addLogMessage(line);
                updateMessage("Durum => " + line);
            }
            inputReader.close();

            // İşlemi bekle ve başarı kodunu dönder
            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (IOException | InterruptedException e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
            LoggerService.addLogMessage("Bir hata oluştu.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("İndirme Sırasında bir hata oluştu ");
            alert.show();
            updateMessage("Hata: " + e.getMessage());
            return false;
        }
    }
}
