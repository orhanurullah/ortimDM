package com.ortimdm.ortimdm.task;

import com.ortimdm.ortimdm.model.AnalyzeUrl;
import com.ortimdm.ortimdm.service.CreateCommandYtDlp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class AnalyzeTask extends Task<ObservableList<AnalyzeUrl>> {

    private final String url;

    public AnalyzeTask(String url) {
        this.url = url;
    }

    @Override
    protected ObservableList<AnalyzeUrl> call() throws Exception {
        Process process = null;
        try {
            System.out.println(url);
            String[] command = {"yt-dlp", "--get-thumbnail", "--get-filename", "--get-description", "--simulate", "--encoding", "UTF-8", url};
            process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader inputReader = new BufferedReader(inputStreamReader);
            String line;
            ObservableList<AnalyzeUrl> analyzeUrls = FXCollections.observableArrayList();
            while ((line = inputReader.readLine()) != null) {
                AnalyzeUrl analyzeUrl = new AnalyzeUrl();
                System.out.println(line);
                if (line.startsWith("Thumbnail")) {
                    analyzeUrl.setImageLink(line.substring(line.indexOf(":") + 1).trim());
                    System.out.println("Image Link => " + analyzeUrl.getImageLink());
                } else line = CreateCommandYtDlp.getAttribute(line, analyzeUrl);
                analyzeUrls.add(analyzeUrl);
            }
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
            return analyzeUrls;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

}
