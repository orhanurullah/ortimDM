package com.ortimdm.ortimdm.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadRequiredApps {

    public DownloadRequiredApps(){
        System.out.println("Gerekli dosyalar indirilecek...");
        LoggerService.addLogMessage("Yüklü değilse gerekli dosyalar indirilecek...");
    }

    public void downloadFfmpeg(){
        String ffmpegUrl = "https://github.com/FFmpeg/FFmpeg/archive/refs/heads/release/6.1.zip";
        String ffmpegDownloadPath = "ffmpeg.zip";
        String ffmpegDestinationPath = "C:/ffmpeg";
        String ffmpegExecutablePath = ffmpegDestinationPath + "/ffmpeg.exe";
        if(Files.exists(Paths.get(ffmpegDestinationPath))){
            System.out.println("yüklü olduğu için ffmpeg indirilmeyecek.");
            LoggerService.addLogMessage("FFMPEG yüklü...");
            return;
        }
        try {
            // FFmpeg dosyasını indir ve çıkart
            downloadAndExtract(ffmpegUrl, ffmpegDownloadPath, ffmpegDestinationPath);

            // İşletim sistemi uygun erişim izinleri ver
            grantExecutionPermission(ffmpegExecutablePath);

            System.out.println("FFmpeg başarıyla indirildi ve yüklendi.");
            LoggerService.addLogMessage("FFmpeg başarıyla indirildi ve yüklendi.");
        } catch (IOException e) {
            LoggerService.addLogMessage("FFmpeg indirme sırasında bir hata oluştu.");
            e.printStackTrace();
        }
    }

    public static void downloadAndExtract(String fileUrl, String downloadPath, String destinationPath) throws IOException {
        downloadFile(fileUrl, downloadPath);

        unzipFile(downloadPath, destinationPath);
    }

    public static void downloadFile(String fileUrl, String savePath) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(savePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("Dosya başarıyla indirildi: " + savePath);
            LoggerService.addLogMessage("Dosya başarıyla indirildi.");
        } else {
            System.out.println("İndirme hatası: HTTP hata kodu " + responseCode);
            LoggerService.addLogMessage("İndirme hatası: HTTP hata kodu " + responseCode);
        }
        httpConn.disconnect();
    }

    public static void unzipFile(String zipFilePath, String destinationDir) throws IOException {
        Path destDir = Paths.get(destinationDir);
        if (!Files.exists(destDir)) {
            Files.createDirectories(destDir);
        }

        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                String filePath = destinationDir + File.separator + entryName;

                if (entry.isDirectory()) {
                    Files.createDirectories(Paths.get(filePath));
                } else {
                    Path extractFilePath = Paths.get(filePath);
                    Files.createDirectories(extractFilePath.getParent());

                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
        System.out.println("Dosya başarıyla çıkartıldı: " + zipFilePath);
    }

    public static void grantExecutionPermission(String filePath) {
        File file = new File(filePath);
        file.setExecutable(true);
    }

    public void downloadYtDlp(){
        String message = null;
        try {
            message = "";
            Path downloadPath = Paths.get("yt-dlp.exe");
            if (Files.exists(downloadPath)) {
                message += "yt-dlp programı hali hazırda kurulu olduğundan metottan çıkılıyor...\n";
                LoggerService.addLogMessage("yt-dlp programı kurulu...");
                System.out.println(message);
                return;
            }
            String downloadCommand = "curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe -o yt-dlp.exe";
            // yt-dlp'yi indir
            Process downloadProcess = Runtime.getRuntime().exec(downloadCommand);
            downloadProcess.info();
            downloadProcess.waitFor();

            // Sistem yoluna ekleme komutunu oluştur
            String addToPathCommand = "setx PATH \"%PATH%;%cd%\"";

            // Sistem yoluna yt-dlp'yi ekle
            Process addToPathProcess = Runtime.getRuntime().exec(addToPathCommand);
            addToPathProcess.info();
            addToPathProcess.waitFor();

            message += "yt-dlp başarıyla yüklendi ve sistem yoluna eklendi.\n";
            LoggerService.addLogMessage("yt-dlp programı yüklendi ve ortam değişkenlerine kaydedildi.");
            System.out.println(message);

        } catch (IOException | InterruptedException e) {
            message += "yt-dlp yükleme esnasında bir hata oluştu => " + e.getMessage();
            LoggerService.addLogMessage("Dikkat! program yüklemesinde bir hata oluştu.");
            System.out.println(message);
        }
    }
}
