package br.com.alura.screenmatchspring.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiConsumer {
    public static HttpResponse<String> getData(String url) {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;

        try {
             response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (RuntimeException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return  response;
    }
}
