package aic13.group6.topic2.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.NoSuchElementException;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.scrapper.YFinanceScrapper;

public class App {

	public static void main(String...args) throws SQLException {
		TimerTask getPagesHourly = new TimerTask() {

            @Override
            public void run() {
                YFinanceScrapper grabber = new YFinanceScrapper();
                DAOArticle daoArticle = new DAOArticle();
        
                ArrayList<String> list = grabber.getURLs();
                
                Iterator<String> i = list.iterator();
                while(i.hasNext()) {
                    String url = i.next();
                    try {
                    	Article article = new Article();
                    	article.setUrl(url);
                    	
                    	if(daoArticle.findByID(article) == null) {
	                    	article = grabber.getPage(url);
	                        daoArticle.create(article);
                    	}
                        
                    } catch (NoSuchElementException nsee) {
                        System.out.println("URL couldn't be parsed: " + url);
                        System.out.println(nsee.getMessage());
                    } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


                }
            }
            
        };
        
        Timer timer = new Timer();
        
        // start immediatly and rerun every hour
        timer.schedule(getPagesHourly, 0l, 1000 * 60 * 60);
	}
}
