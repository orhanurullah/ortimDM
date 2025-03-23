package com.ortimdm.ortimdm.service;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class LicenceService {

    private static final String LICENCE_FILE = "licence.ortim";

    public String generateLicenseKey() {
        try {
            String osName = System.getProperty("os.name");
            String osArch = System.getProperty("os.arch");
            byte[] macAddressBytes = getMacAddress();

            String combinedInfo = osName + osArch + Arrays.toString(macAddressBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(combinedInfo.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("license key not created");
            e.printStackTrace();
            return null;
        }
    }

    public void saveLicenseKey(String licenseKey) throws IOException {
        File licenceFile = new File(LICENCE_FILE);
        if(!Files.exists(licenceFile.toPath())){
            Files.createFile(licenceFile.toPath());
        }
        FileWriter fileWriter = new FileWriter(licenceFile);
        fileWriter.write(licenseKey);
        fileWriter.close();
        System.out.println(licenseKey + " verisi yazıldı.");
    }

    public boolean validateLicenseKey(String licenseKey) throws IOException {
        FileReader fileReader = new FileReader(LICENCE_FILE);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        StringBuilder key = new StringBuilder();
        while ((line = reader.readLine())!= null){
            key.append(line).append("\n");
        }
        System.out.println("Lisans Anahtarı: " + key);

        return key.toString().trim().equalsIgnoreCase(licenseKey);
    }

    private byte[] getMacAddress() {
        return new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB};
    }

}
