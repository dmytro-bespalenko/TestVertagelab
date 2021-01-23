package com.hfad.testvertagelab;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;


public class RestClient {
    private static final String HTTPS_STRING = "https";

    String REST_SERVER_HTTPS_GET_URI = "https://2fjd9l3x1l.api.quickmocker.com/kyiv/places";


    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public String getRequest() {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(REST_SERVER_HTTPS_GET_URI.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(REST_SERVER_HTTPS_GET_URI);
        HttpGet getMethod = new HttpGet(newURI);
        try {
            assert httpClient != null;
            response = httpClient.execute(getMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }
}