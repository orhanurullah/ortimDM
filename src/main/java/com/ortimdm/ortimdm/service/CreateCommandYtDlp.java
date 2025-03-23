package com.ortimdm.ortimdm.service;

import com.ortimdm.ortimdm.component.AnalyzeListCell;
import com.ortimdm.ortimdm.model.AnalyzeUrl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

public class CreateCommandYtDlp {

    private final StringBuilder flag = new StringBuilder();

    private String savePath;

    private String url;

    public CreateCommandYtDlp(String url){
        if(!controlUrl()){
            LoggerService.addLogMessage("Url is not valid");
            return;
        }
        this.url = url;
    }

    public void addFlag(String flag){
        this.flag.append(flag);
    }

    public StringBuilder getFlag(){
        return this.flag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return Objects.requireNonNullElse(this.savePath, "");
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private boolean controlUrl(){
        return this.url != null && !this.url.isEmpty() && this.url.startsWith("http");
    }

    public String getCommand(){
        String prefix = "yt-dlp";
        return prefix + " " + this.flag + " " + this.getSavePath() + this.getUrl();
    }

    public Task<ObservableList<AnalyzeUrl>> urlInfo(){
        String url = this.getUrl();
        return new Task<ObservableList<AnalyzeUrl>>() {
            @Override
            protected ObservableList<AnalyzeUrl> call() throws Exception {
                System.out.println(url);
                String[] command = {"yt-dlp", "--get-info", "--get-thumbnail", "--get-filename", "--get-description", "--simulate" , "--encoding", "UTF-8", url};

                Process process = Runtime.getRuntime().exec(command);
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader inputReader = new BufferedReader(inputStreamReader);
                String line;
                int videoCount = 0;
                ObservableList<AnalyzeUrl> analyzeUrls = FXCollections.observableArrayList();
                while ((line = inputReader.readLine()) != null) {
                    AnalyzeUrl analyzeUrl = new AnalyzeUrl();
                    if (line.trim().startsWith("[info]")) {
                        // Çıktıdaki "info" satırlarını işle
                        if (line.contains("entries")) {
                            // "entries" kelimesini içeren satırlar video listesiyle ilgilidir
                            int startIndex = line.indexOf('[');
                            int endIndex = line.lastIndexOf(']');
                            String entriesStr = line.substring(startIndex + 1, endIndex);
                            // Virgül karakterine göre ayrılan girişlerin sayısını hesapla
                            videoCount = entriesStr.split(",").length;
                        }
                    }
                    if (line.startsWith("Thumbnail")) {
                        analyzeUrl.setImageLink(line.substring(line.indexOf(":") + 1).trim());
                        System.out.println("İmage Link => " + analyzeUrl.getImageLink());
                    } else {
                        line = getAttribute(line, analyzeUrl);
                    }
                    analyzeUrls.add(analyzeUrl);
                    System.out.println(line);
                    System.out.println("Video Count = " + videoCount);
                }
                int exitCode = process.waitFor();
                System.out.println("Çıkış kodu: " + exitCode);
                return analyzeUrls;
            }
        };
    }

    public static String getAttribute(String line, AnalyzeUrl analyzeUrl) {
        if (line.startsWith("Filename")) {
            line = line.replaceAll("\\[.*?]", "");
            String[] parts = line.split("\\.");
            analyzeUrl.setFilename(parts[0].trim().toLowerCase(Locale.ROOT));
            analyzeUrl.setFormat(parts[1].trim().toLowerCase(Locale.US));
            System.out.println(analyzeUrl.getFilename() + "." + analyzeUrl.getFormat());
        } else if (line.startsWith("Description")) {
            analyzeUrl.setDescription(line.substring(line.indexOf(":") + 1).trim());
            System.out.println("Description => " + analyzeUrl.getDescription());
        }
        return line;
    }
}
