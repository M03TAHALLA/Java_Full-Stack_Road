package com.personal_blog.personal_blog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_blog.personal_blog.model.Article;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ArticleService {

    private final ObjectMapper objectMapper;
    private final Path dataDirectory;

    public ArticleService(ObjectMapper objectMapper, @Value("${blog.data.path:data/articles}") String dataPath) {
        this.objectMapper = objectMapper;
        this.dataDirectory = Paths.get(dataPath);
    }

    // Create data/articles directory automatically when the app starts.
    @PostConstruct
    public void initDataDirectory() {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create article data directory: " + dataDirectory, exception);
        }
    }

    // Read all article JSON files from the configured folder.
    public List<Article> getAllArticles() {
        if (Files.notExists(dataDirectory)) {
            return new ArrayList<>();
        }

        try (Stream<Path> files = Files.list(dataDirectory)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .map(this::readArticleFile)
                    .sorted(Comparator.comparing(Article::getPublishedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read articles from: " + dataDirectory, exception);
        }
    }

    // Read one article file by slug (for example: my-first-post -> my-first-post.json).
    public Article getArticleBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return null;
        }

        Path articlePath = getArticleFilePath(slug);
        if (Files.notExists(articlePath)) {
            return null;
        }

        try {
            return objectMapper.readValue(articlePath.toFile(), Article.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read article file: " + articlePath, exception);
        }
    }

    // Save article as JSON. This method handles both create and update.
    public void saveArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new IllegalArgumentException("Article title cannot be empty");
        }

        if (article.getId() == null || article.getId().isBlank()) {
            article.setId(UUID.randomUUID().toString());
        }

        String slug = generateSlug(article.getTitle());
        if (slug.isBlank()) {
            throw new IllegalArgumentException("Article title must contain letters or numbers");
        }
        article.setSlug(slug);

        if (article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }

        Path articlePath = getArticleFilePath(article.getSlug());

        try {
            Files.createDirectories(dataDirectory);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(articlePath.toFile(), article);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to save article file: " + articlePath, exception);
        }
    }

    // Delete one article file by slug.
    public void deleteArticle(String slug) {
        if (slug == null || slug.isBlank()) {
            return;
        }

        Path articlePath = getArticleFilePath(slug);
        try {
            Files.deleteIfExists(articlePath);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to delete article file: " + articlePath, exception);
        }
    }

    // Convert a title to a URL-friendly slug.
    public String generateSlug(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }

        // Remove accents and then keep only letters, numbers, spaces and dashes.
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", " ")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    private Article readArticleFile(Path filePath) {
        try {
            return objectMapper.readValue(filePath.toFile(), Article.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to parse article file: " + filePath, exception);
        }
    }

    private Path getArticleFilePath(String slug) {
        return dataDirectory.resolve(slug + ".json");
    }
}
