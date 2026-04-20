package com.personal_blog.personal_blog.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Article {

	private String id;
	private String title;
	private String slug;
	private String content;
	private LocalDateTime publishedAt;

	// Default constructor needed by Jackson when reading JSON files.
	public Article() {
		this.id = UUID.randomUUID().toString();
	}

	// Full constructor for creating/updating an article with all values.
	public Article(String id, String title, String slug, String content, LocalDateTime publishedAt) {
		this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
		this.title = title;
		this.slug = slug;
		this.content = content;
		this.publishedAt = publishedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}
}
