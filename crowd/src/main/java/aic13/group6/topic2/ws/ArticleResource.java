package aic13.group6.topic2.ws;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.daos.DAOTag;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Tag;
import aic13.group6.topic2.scrapper.YFinancePageScrapperJava;
import aic13.group6.topic2.scrapper.YFinanceScrapperPhantomJS;
import aic13.group6.topic2.scrapper.YFinancePageScrapperYQL;
import aic13.group6.topic2.scrapper.YFinanceSearchUrlScrapperYQL;

@Path("/article")
public class ArticleResource {
	
	private static final Logger logger =
			Logger.getLogger(TagResource.class.getName());
	
	@GET
	@Path("{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Article> get(@PathParam("name") String name) {
		
//		YFinanceScrapper grabber = new YFinanceScrapper();
		YFinanceSearchUrlScrapperYQL urlScrapper = new YFinanceSearchUrlScrapperYQL();
//		YFinancePageScrapperYQL pageScrapper = new YFinancePageScrapperYQL();
		YFinancePageScrapperJava pageScrapper = new YFinancePageScrapperJava();
        DAOArticle daoArticle = new DAOArticle();

//        ArrayList<String> list = grabber.getURLs();
        ArrayList<String> list = urlScrapper.getArticleUrlsForCompany(name);
        ArrayList<Article> articles = new ArrayList<Article>();
        
        Iterator<String> i = list.iterator();
        while(i.hasNext()) {
            String url = i.next();
            try {
            	Article article = new Article();
            	article.setUrl(url);
            	
            	if(daoArticle.findByID(article) == null) {
//                	article = grabber.getPage(url);
            		article = pageScrapper.getPage(url);
            		Tag tag = new Tag();
            		tag.setName(url);
            		DAOTag daoTag = new DAOTag();
            		tag = daoTag.create(tag);
            		article.getTags().add(tag);
                    article = daoArticle.create(article);
                    articles.add(article);
            	}
                
            } catch (NoSuchElementException nsee) {
            	// TODO throw nice exception and handle problem
                System.out.println("URL couldn't be parsed: " + url);
                System.out.println(nsee.getMessage());
            } catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
            	return articles;
//			} catch (UnreachableBrowserException ube) {
//				// TODO throw nice exception and handle url
//				System.err.println("PHANTOMJS CRASHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//				System.err.println("URL: " + url);
////				grabber.quit();
//				return articles;
			}


        }
        
//        grabber.quit();
        return articles;
		
	}
	
	@GET
	@Path("tag/{number}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Article getArticle(@PathParam("number") Integer number) throws SQLException {
		Article article = new Article();
		article.setDate(1l);
		article.setUrl("http://example.com/" + number);
		article.setTitle("test article");
		
		Tag tag = new Tag();
		tag.setName("tag" + number);
		DAOTag daoTag = new DAOTag();
//		tag = daoTag.create(tag);
		tag.getArticles().add(article);
		article.getTags().add(tag);
//		tagList.add(tag);
		
		DAOArticle daoArticle = new DAOArticle();
		daoArticle.create(article);
		
		return article;
		
	}

}
