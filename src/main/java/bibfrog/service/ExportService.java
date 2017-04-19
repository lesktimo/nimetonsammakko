
package bibfrog.service;

import bibfrog.domain.Article;
import bibfrog.domain.Book;
import bibfrog.domain.Inproceeding;
import bibfrog.domain.Reference;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class ExportService {
 
    
    public void createFile(String bibtex) throws IOException{
        File file = new File("bibtex.bib");
        file.createNewFile();
        
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(bibtex);
        
        fileWriter.flush();
        fileWriter.close();
        
    }
    
    
    public String createBibtexFromInproceeding(Inproceeding inpro) {
        String bibtex = "@inproceedings{" + inpro.getReferenceKey() + ","
                + "\n   author = {" + inpro.authorString() + "},"
                + "\n   title = {" + inpro.getTitle() + "},"
                + "\n   booktitle = {" + inpro.getBookTitle() + "},"
                + "\n   year = {" + inpro.getPublishYear() + "}";
        bibtex += addOptionalFieldsToBibtex(inpro);
        bibtex += "\n}";
        return bibtex;
    }
    
    public String addOptionalFieldsToBibtex ( Reference ref) {
        HashMap<String, String> optionalFields = ref.optionalFields();
        String inproTex = "";
        
        for (Entry entry : optionalFields.entrySet()) {
            if (entry.getValue() != null) {
                inproTex += ",\n    " + entry.getKey() + "= {" + entry.getValue() + "}";
            }
        }
        return inproTex;
    }
    
    
    
    public String createBibtexFromBookFile (Book book){
        return "";
    }
    
    public String createBibtexFromArticleFile(Article article){
        return "";
    }
    
    
}
