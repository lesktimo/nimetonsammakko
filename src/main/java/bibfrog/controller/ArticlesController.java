package bibfrog.controller;

import bibfrog.domain.Article;
import bibfrog.repositories.ArticleRepo;
import bibfrog.service.*;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticlesController {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ExportService exportService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/article/add", method = RequestMethod.GET)
    public String addArticle(Model model) {
        model.addAttribute("article", new Article());
        return "article";
    }

    @RequestMapping(value = "/article/{id}/edit", method = RequestMethod.GET)
    public String editArticle(Model model, @PathVariable long id) {
        model.addAttribute("article", articleRepo.findOne(id));
        return "article_edit";
    }

    @RequestMapping(value = "/article/{id}/edit", method = RequestMethod.POST)
    public String updateArticle(@PathVariable Long id, @Valid @ModelAttribute Article article, BindingResult bindingResult) {
        articleRepo.delete(id);

        if (bindingResult.hasErrors()) {
            return "article_edit";
        }
        return setArticleAttributes(article);
    }
    
    @RequestMapping(value = "/article/{id}/delete", method = RequestMethod.DELETE)
    public String deleteArticle(@PathVariable Long id) {
        articleRepo.delete(id);
        return "redirect:/articles";
    }

    @RequestMapping(value = "/article/add", method = RequestMethod.POST)
    public String postArticle(@Valid @ModelAttribute Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article";
        }
        return setArticleAttributes(article);
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public String listArticles(Model model) {
        model.addAttribute("articleList", articleRepo.findAll());
        return "articles";
    }

    @RequestMapping(value = "/article/{id}/download", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadArticle(@PathVariable Long id, @RequestParam String fileName) throws IOException {
        createFileForDownloading(id);
        return fileService.createBibFile(fileName);
    }

    @RequestMapping(value = "/articles/all/download", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadAllArticles(@RequestParam String fileName) throws IOException {
        String bibtex = exportService.createBibtexFromAllArticles(articleRepo.findAll());
        exportService.createFile(bibtex);
        return fileService.createBibFile(fileName);
    }

    private void createFileForDownloading(Long id) throws IOException {
        Article article = articleRepo.findOne(id);
        String bibtex = exportService.createBibtexFromArticle(article);
        exportService.createFile(bibtex);
    }
    
    private String setArticleAttributes(Article article) {
        article = articleRepo.save(article);
        if (article.getReferenceKey() == null || article.getReferenceKey().isEmpty()) {
            article.generateReferenceKey();
            articleRepo.save(article);
        }
        return "redirect:/articles";
    }

}
