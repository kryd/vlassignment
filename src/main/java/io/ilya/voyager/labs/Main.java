package io.ilya.voyager.labs;

import io.ilya.voyager.labs.service.DataExtractorService;

public class Main {
    public static void main(String[] args) {
        String initialUrl = "https://www.ynetnews.com";
        int maxDepth = 2;
        int maxUrlsPerPage = 5;
        boolean isUnique = true;

        DataExtractorService service = new DataExtractorService(maxDepth, maxUrlsPerPage, initialUrl, isUnique);
        service.extractDataToFiles();
    }
}