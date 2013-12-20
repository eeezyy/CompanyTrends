package aic13.group6.topic2.ws;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import aic13.group6.topic2.daos.DAOArticleJPA;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.scrapper.YFinanceScrapper;

@Path("/article")
public class ArticleResource {
	
	private static final Logger logger =
			Logger.getLogger(TagResource.class.getName());
	
	@GET
    @Produces({"application/xml", "application/json"})
    public List<Article> get() {
		
		YFinanceScrapper grabber = new YFinanceScrapper();
        DAOArticleJPA daoArticle = new DAOArticleJPA();

        ArrayList<String> list = grabber.getURLs();
        ArrayList<Article> articles = new ArrayList<Article>();
        
        Iterator<String> i = list.iterator();
        while(i.hasNext()) {
            String url = i.next();
            try {
            	Article article = new Article();
            	article.setUrl(url);
            	
            	if(daoArticle.findByUrl(article) == null) {
                	article = grabber.getPage(url);
                    daoArticle.create(article);
                    articles.add(article);
            	}
                
            } catch (NoSuchElementException nsee) {
            	// TODO throw nice exception and handle problem
                System.out.println("URL couldn't be parsed: " + url);
                System.out.println(nsee.getMessage());
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnreachableBrowserException ube) {
				// TODO throw nice exception and handle url
				System.err.println("PHANTOMJS CRASHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.err.println("URL: " + url);
				grabber.quit();
				return articles;
			}


        }
        
        grabber.quit();
        return articles;
		
	}

}
