package com.cdac.service;

import java.util.ArrayList;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cdac.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class ScraperService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> fetchAndCompareProducts(String query) {
        List<Product> products = new ArrayList<>();

        // --- Flipkart data ---
        try {
            String flipkartUrl = "http://localhost:3000/search/" + query;
            ResponseEntity<String> flipkartResponse = restTemplate.getForEntity(flipkartUrl, String.class);
            JSONObject flipkartJson = new JSONObject(flipkartResponse.getBody());
            JSONArray flipkartResults = flipkartJson.getJSONArray("result");

            for (int i = 0; i < flipkartResults.length(); i++) {
                JSONObject item = flipkartResults.getJSONObject(i);
                String title = item.optString("name");
                String link = item.optString("link");
                double price = item.optDouble("current_price", 0.0);

                Product product = new Product(title, "Flipkart", price, link);
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- Amazon data ---
        try {
            String amazonUrl = "http://localhost:8085/search?q=" + query;
            ResponseEntity<String> amazonResponse = restTemplate.getForEntity(amazonUrl, String.class);
            JSONObject amazonJson = new JSONObject(amazonResponse.getBody());
            JSONArray amazonResults = amazonJson.getJSONArray("results");

            for (int i = 0; i < amazonResults.length(); i++) {
                JSONObject item = amazonResults.getJSONObject(i);
                String title = item.optString("title");
                String link = item.optString("url");
                String priceStr = item.optString("price").replaceAll("[^\\d.]", "");
                double price = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);

                Product product = new Product(title, "Amazon", price, link);
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sort by price (lowest first)
        products.sort(Comparator.comparingDouble(Product::getCost));
        return products;
    }
}
