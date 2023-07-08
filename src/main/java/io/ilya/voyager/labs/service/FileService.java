package io.ilya.voyager.labs.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {

    public void saveToFile(String htmlContent, int depth, String urlStr) {
        try {
            String currentWorkingDirectory = System.getProperty("user.dir");
            Path buildDirectoryPath = Paths.get(currentWorkingDirectory, "build");
            Path targetPath = Path.of(buildDirectoryPath.toString(), "htmls", String.valueOf(depth), getFileName(urlStr));
            Files.createDirectories(targetPath.getParent());
            Files.writeString(targetPath, htmlContent, StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String urlStr) {
        String normalizedFileName = urlStr.replaceAll("[/:*?\"<>|.]", "_");
        return normalizedFileName + ".html";
    }
}
