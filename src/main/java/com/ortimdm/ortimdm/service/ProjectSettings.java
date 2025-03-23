package com.ortimdm.ortimdm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectSettings {

    public void createRequiredFolders(String mainFolder, String folderName) throws IOException {
        LoggerService.addLogMessage("Gerekli klasörler oluşturuluyor... " + folderName);
        Path downloadsPath = Paths.get(System.getProperty("user.home"), mainFolder);
        String replaceFolder = folderName.replace(" ", "-");
        Path folderPath = downloadsPath.resolve(replaceFolder);
        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
            System.out.println("Klasör başarıyla oluşturuldu: " + replaceFolder);
        }else{
            System.out.println(mainFolder + " isimli klasörde " + replaceFolder + " isimli klasör mevcut");
        }
    }
}
