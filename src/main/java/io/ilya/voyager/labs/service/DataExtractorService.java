package io.ilya.voyager.labs.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataExtractorService {
    private final int maxDepth;
    private final int maxUrlsPerPage;
    private final String baseUrl;
    private final boolean isUnique;

    private ExecutorService executorService;
    private URLExtractorService urlExtractorService;
    private FileService fileService;

    public DataExtractorService(int maxDepth, int maxUrlsPerPage, String baseUrl, boolean isUnique) {
        this.maxDepth = maxDepth;
        this.maxUrlsPerPage = maxUrlsPerPage;
        this.baseUrl = baseUrl;
        this.isUnique = isUnique;
    }

    public void extractDataToFiles() {
        int maxNumOfThreads = Runtime.getRuntime().availableProcessors() * maxUrlsPerPage; // this parameter should be checked for optimization while running the program
        int currentDepth = 0;

        executorService = Executors.newFixedThreadPool(maxNumOfThreads);
        urlExtractorService = new URLExtractorService();
        fileService = new FileService();

        extractProcess(baseUrl, currentDepth);

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Extraction completed!");
    }

    private void extractProcess(String url, int currentDepth) {
        if (currentDepth > maxDepth+3) {
            return;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            String htmlContent = extractHtml(doc);
            if (currentDepth == 0) {
                fileService.saveToFile(htmlContent, currentDepth, url);
            }

            List<String> extractedUrls = urlExtractorService.extractUrls(doc, maxUrlsPerPage, isUnique);

            for (String extractedUrl : extractedUrls) {
                final int nextDepth = currentDepth + 1;
                if (nextDepth <= maxDepth) {
                    fileService.saveToFile(htmlContent, nextDepth, extractedUrl);
                    if (!executorService.isShutdown()) {
                        executorService.execute(() -> {
                            extractProcess(extractedUrl, nextDepth);
                        });
                    }
                }
            }

            System.out.println("\nextractedUrls");
            extractedUrls.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractHtml(Document document) {
        return document.outerHtml();
    }
}
