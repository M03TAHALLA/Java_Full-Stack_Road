package com.personal_blog.personal_blog.controller;

import com.personal_blog.personal_blog.model.Article;
import com.personal_blog.personal_blog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicController {

    private final ArticleService articleService;

    public PublicController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Home page: show all articles (newest first, already sorted in ArticleService).
    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "public/home";
    }

    // Article page: show one article by slug.
    @GetMapping("/article/{slug}")
    public String showArticlePage(@PathVariable String slug, Model model) {
        Article article = articleService.getArticleBySlug(slug);

        if (article == null) {
            return "redirect:/";
        }

        model.addAttribute("article", article);
        return "public/article";
    }

    // Custom login page used by Spring Security.
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
