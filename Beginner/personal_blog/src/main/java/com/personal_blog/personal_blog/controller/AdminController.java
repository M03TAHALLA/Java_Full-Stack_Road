package com.personal_blog.personal_blog.controller;

import com.personal_blog.personal_blog.model.Article;
import com.personal_blog.personal_blog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final ArticleService articleService;

    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Dashboard page: show all articles with edit/delete actions.
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "admin/dashboard";
    }

    // Show empty form to create a new article.
    @GetMapping("/admin/articles/new")
    public String showAddArticleForm() {
        return "admin/add-article";
    }

    // Handle add article form submit.
    @PostMapping("/admin/articles")
    public String createArticle(@RequestParam String title,
                                @RequestParam String content,
                                Model model) {
        if (title == null || title.isBlank()) {
            model.addAttribute("error", "Title is required.");
            model.addAttribute("title", title);
            model.addAttribute("content", content);
            return "admin/add-article";
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);

        articleService.saveArticle(article);
        return "redirect:/admin/dashboard";
    }

    // Show edit form with existing article data.
    @GetMapping("/admin/articles/{slug}/edit")
    public String showEditArticleForm(@PathVariable String slug, Model model) {
        Article article = articleService.getArticleBySlug(slug);

        if (article == null) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("article", article);
        return "admin/edit-article";
    }

    // Handle update article form submit.
    @PostMapping("/admin/articles/{slug}/edit")
    public String updateArticle(@PathVariable String slug,
                                @RequestParam String title,
                                @RequestParam String content,
                                Model model) {
        Article existingArticle = articleService.getArticleBySlug(slug);

        if (existingArticle == null) {
            return "redirect:/admin/dashboard";
        }

        if (title == null || title.isBlank()) {
            existingArticle.setContent(content);
            model.addAttribute("article", existingArticle);
            model.addAttribute("error", "Title is required.");
            return "admin/edit-article";
        }

        String newSlug = articleService.generateSlug(title);

        existingArticle.setTitle(title);
        existingArticle.setContent(content);
        articleService.saveArticle(existingArticle);

        // If title changed, slug can change. Remove old file to avoid duplicates.
        if (!slug.equals(newSlug)) {
            articleService.deleteArticle(slug);
        }

        return "redirect:/admin/dashboard";
    }

    // Handle delete action.
    @PostMapping("/admin/articles/{slug}/delete")
    public String deleteArticle(@PathVariable String slug) {
        articleService.deleteArticle(slug);
        return "redirect:/admin/dashboard";
    }
}
