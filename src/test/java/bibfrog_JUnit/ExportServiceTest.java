package bibfrog_JUnit;

import bibfrog.domain.*;
import bibfrog.service.ExportService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExportServiceTest {

    ExportService es;

    @Before
    public void setUp() {
        es = new ExportService();
    }

    @Test
    public void createBibtexFromInproWorks() {
        Inproceeding inpro = new Inproceeding();
        inpro.setReferenceKey("KEY");
        inpro.setGivenAuthors("Author");
        inpro.setAuthors();
        inpro.setTitle("Title");
        inpro.setBookTitle("Booktitle");
        inpro.setPublishYear(2017);
        String expected = "@inproceedings{KEY,\n"
                + "author = {Author},\n"
                + "title = {Title},\n"
                + "booktitle = {Booktitle},\n"
                + "year = {2017}\n"
                + "}";
        assertEquals(expected, es.createBibtexFromInproceeding(inpro));
    }

    @Test
    public void createBibtexFromBookWorks() {
        Book book = new Book();
        book.setReferenceKey("KEY");
        book.setGivenAuthors("Author");
        book.setAuthors();
        book.setTitle("Title");
        book.setPublisher("Publisher");
        book.setPublishYear(2017);
        String expected = "@book{KEY,\n"
                + "author = {Author},\n"
                + "title = {Title},\n"
                + "publisher = {Publisher},\n"
                + "year = {2017}\n"
                + "}";
        assertEquals(expected, es.createBibtexFromBook(book));
    }

    @Test
    public void createBibtexFromArticleWorks() {
        Article article = setArticle();
        String expected = "@article{KEY,\n"
                + "author = {Author},\n"
                + "title = {Title},\n"
                + "journal = {Journal},\n"
                + "year = {2017}\n"
                + "}";
        assertEquals(expected, es.createBibtexFromArticle(article));
    }

    @Test
    public void optionalFieldsAreAddedCorrectly() {
        Article article = setArticle();
        article.setPages("1--2");
        article.setPublishMonth(8);
        
        String expected = "@article{KEY,\n"
                + "author = {Author},\n"
                + "title = {Title},\n"
                + "journal = {Journal},\n"
                + "year = {2017},\n"
                + "pages = {1--2},\n"
                + "month = {8}\n"
                + "}";
        assertEquals(expected, es.createBibtexFromArticle(article));
    }
    
//    @Test
//    public void everyOptionalFieldIsAddedCorrectly() {
//        Article article = setArticle();
//        article.setVolume(1);
//        article.setNumber(2);
//        article.setPages("1--2");
//        article.setPublishMonth(8);
//        article.setNote("Note");
//        
//        String expected = "@article{KEY,\n"
//                + "author = {Author},\n"
//                + "title = {Title},\n"
//                + "journal = {Journal},\n"
//                + "year = {2017},\n"
//                + "volume = {1},\n"
//                + "number = {2},\n"
//                + "pages = {1--2},\n"
//                + "month = {8},\n"
//                + "note = {Note}\n"
//                + "}";
//        assertEquals(expected, es.createBibtexFromArticle(article));
//    }

    @Test
    public void scandicCheckerDoesntChangeIfNoScandics() {
        assertEquals("Some string", es.scandicChecker("Some string"));
    }
    
    @Test
    public void scandicCheckerChangesSmallScandics() {
        assertEquals("String c\\\"ont\\\"aining \\aa", es.scandicChecker("String cöntäining å"));
    }
    
    @Test
    public void scandicCheckerChangesCapitalScandics() {
        assertEquals("String c\\\"Ont\\\"Aining \\AA", es.scandicChecker("String cÖntÄining Å"));
    }
    
    private Article setArticle() {
        Article article = new Article();
        article.setReferenceKey("KEY");
        article.setGivenAuthors("Author");
        article.setAuthors();
        article.setTitle("Title");
        article.setJournal("Journal");
        article.setPublishYear(2017);

        return article;
    }
}
