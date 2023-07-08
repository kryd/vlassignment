package io.ilya.voyager.labs.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileServiceTest {

    private static FileService fileService;

    @BeforeAll
    static void setup() {
        fileService = new FileService();
    }

    @Test
    void saveToFile() {
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        fileService.saveToFile(htmlContent,1,"www.fox.com");
    }
}