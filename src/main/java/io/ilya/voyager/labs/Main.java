package io.ilya.voyager.labs;

import io.ilya.voyager.labs.service.HtmlExtractorService;

public class Main {
    public static void main(String[] args) {
        String initialUrl = "https://www.ynetnews.com/";
        Thread thread = new Thread(new HtmlExtractorService(initialUrl));
        thread.start();
    }
}