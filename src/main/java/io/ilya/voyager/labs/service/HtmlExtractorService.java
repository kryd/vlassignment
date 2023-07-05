package io.ilya.voyager.labs.service;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class HtmlExtractorService implements Runnable {

    private String url;

    @Override
    public void run() {
        try {
            int numberOfUrls = 5;
            Document doc = Jsoup.connect(url).get();
            // Extract URLs
            List<String> extractedUrls = extractUrls(doc);
            extractedUrls.forEach(System.out::println);

//            // Create a thread pool for URL extraction
//            ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//            // Submit tasks for each extracted URL
//            for (int i = 0; i < numberOfUrls; i++) {
//                String extractedUrl = extractedUrls.get(i);
//                executorService.execute(new HtmlExtractorService(extractedUrl));
//            }
//
//            // Shutdown the executor service
//            executorService.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> extractUrls(Document doc) {
        Elements linkElements = doc.select("a[href]");
        return linkElements
                .stream()
                .map(link -> link.attr("href"))
                .toList();
    }
}
