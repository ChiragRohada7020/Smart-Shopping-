package com.cdac.service;

import com.cdac.entity.Product;
import com.cdac.entity.ProductListWrapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ScraperService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisTemplate<String, ProductListWrapper> redisTemplate;

    public List<Product> fetchAndCompareProducts(String query) {
        String redisKey = "PRODUCTS_" + query.toLowerCase();

        // Step 1: Check Redis cache first
        ProductListWrapper cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            System.out.println("Fetching from Redis Cache...");
            return cached.getProducts();
        }

        List<Product> products = new ArrayList<>();

        // Step 2: Fetch from Flipkart
        try {
            String flipkartUrl = "http://localhost:5000/search/" + query;
            ResponseEntity<String> flipkartResponse = restTemplate.getForEntity(flipkartUrl, String.class);
            JSONObject flipkartJson = new JSONObject(flipkartResponse.getBody());
            JSONArray flipkartResults = flipkartJson.getJSONArray("result");

            for (int i = 0; i < flipkartResults.length(); i++) {
                JSONObject item = flipkartResults.getJSONObject(i);
                String title = item.optString("name");
                String link = item.optString("link");
                String imgurl = item.optString("thumbnail");
                double price = item.optDouble("current_price", 0.0);

                // Extract PID from URL
                String pid = extractFlipkartPid(link);

                Product product = new Product(pid, title, "Flipkart", price, imgurl, link);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println("Flipkart fetch failed: " + e.getMessage());
        }

        // Step 3: Fetch from Amazon
        try {
            String amazonUrl = "http://localhost:8085/search?q=" + query;
            ResponseEntity<String> amazonResponse = restTemplate.getForEntity(amazonUrl, String.class);
            JSONObject amazonJson = new JSONObject(amazonResponse.getBody());
            JSONArray amazonResults = amazonJson.getJSONArray("results");

            for (int i = 0; i < amazonResults.length(); i++) {
                JSONObject item = amazonResults.getJSONObject(i);
                String asin = item.optString("asin");
                String title = item.optString("title");
                String link = item.optString("url");
                String imgurl = item.optString("image");
                String priceStr = item.optString("price").replaceAll("[^\\d.]", "");
                double price = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);

                Product product = new Product(asin, title, "Amazon", price, imgurl, link);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println("Amazon fetch failed: " + e.getMessage());
        }

        // Step 4: Sort and store in Redis
        products.sort(Comparator.comparingDouble(Product::getCost));

        // Cache to Redis
        redisTemplate.opsForValue().set(redisKey, new ProductListWrapper(products));

        return products;
    }

    // Utility to extract Flipkart PID from link
    private String extractFlipkartPid(String url) {
        try {
            String[] parts = url.split("\\?");
            if (parts.length > 1) {
                String[] params = parts[1].split("&");
                for (String param : params) {
                    if (param.startsWith("pid=")) {
                        return param.split("=")[1];
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("PID extract error: " + e.getMessage());
        }
        // fallback
        return UUID.randomUUID().toString();
    }
}
