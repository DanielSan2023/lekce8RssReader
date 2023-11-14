package com.engeto.lekce8RssReader.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @GetMapping("/scifi")
    public ResponseEntity<String> getScifiArticle() {
        String filePath = "scifi.txt";
        return ResponseEntity.ok(readFileContent(filePath));
    }

    @GetMapping("/romantic")
    public ResponseEntity<String> getRomanticArticle() {
        String filePath = "romantic.txt";
        return ResponseEntity.ok(readFileContent(filePath));
    }

    @GetMapping("/historic")
    public ResponseEntity<String> getHistoricArticle() {
        String filePath = "historic.txt";
        return ResponseEntity.ok(readFileContent(filePath));
    }

    // Chcel som to automatizovat  aby som do @GetMapping("/article")
    // zadal len categoriu : scifi,romantic,historic
    // a našiel som takto @RequestParam String category
    //localhost:8082/api/article?category=scifi

    @GetMapping("/article")
    public ResponseEntity<String> getArticle(@RequestParam String category) {
        String filePath = category + ".txt";
        if (!isValidCategory(filePath)) {
            return ResponseEntity.badRequest().body("Neplatná kategorie: " + category);
        }
        return ResponseEntity.ok(readFileContent(filePath));
    }

    private boolean isValidCategory(String filePath) {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        return resource.exists();
    }

    private String readFileContent(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return "Chyba při načítání obsahu souboru.";
        }
    }
}
