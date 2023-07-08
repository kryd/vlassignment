package io.ilya.voyager.labs.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class URLExtractorService {

    Set<String> uniqUrls = Collections.synchronizedSet(new HashSet<>());

    protected List<String> extractUrls(Document document, int numOfUrlsToExtract, boolean isUnique) {
        List<String> finalUrlsList = Collections.emptyList();

        if (numOfUrlsToExtract > 0) {
            Elements linkElements = document.select("a[href]");

            List<String> rawUrls = new ArrayList<>(linkElements
                    .stream()
                    .map(link -> link.attr("href")) //extracting urls from links
                    .filter(this::isValidURL)
                    .toList());

            if (isUnique) {
                Collections.shuffle(rawUrls);            //shuffle list to take different urls
                finalUrlsList = rawUrls.stream()
                        .distinct()                     //removes url duplicates from current depth
                        .filter(this::isUnique)         //uniqueness validation
                        .limit(numOfUrlsToExtract)      //get requested number of ulrs
                        .toList();
            } else {
                finalUrlsList = rawUrls
                        .stream()
                        .limit(numOfUrlsToExtract)
                        .toList();
            }

            addUrlsToSet(finalUrlsList); // adding chosen urls to set for future uniqueness validation
        }

        return finalUrlsList;
    }

    private boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private synchronized void addUrlsToSet(List<String> finalUrlsList) {
        uniqUrls.addAll(finalUrlsList);
    }

    private synchronized boolean isUnique(String urlStr) {
        return !uniqUrls.contains(urlStr);
    }
}
