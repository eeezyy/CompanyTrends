package aic13.group6.topic2.scrapper;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.sun.jna.platform.dnd.GhostedDragImage;

import aic13.group6.topic2.entities.Article;

/**
 *	Scrappes the finance news article issued by Yahoo!
 */
public class YFinanceScrapper {
    
//    private final WebDriver driver = new FirefoxDriver();
//	private final WebDriver driver = new HtmlUnitDriver();
    private static WebDriver driver;
    
    public static final File PHANTOMJS_EXE = new File(System.getProperty("user.dir"), "tools/phantomjs-1.9.2-windows/phantomjs.exe");
    
    static {
    	DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("phantomjs.binary.path", PHANTOMJS_EXE.getAbsolutePath());
        
        driver = new PhantomJSDriver(caps);
    }
    
    private final String baseUrl = "http://finance.yahoo.com/blogs/";
    private final String xpathUrl = "//*/li[contains(concat(' ', normalize-space(@class), ' '), 'content') and not(contains(concat(' ', normalize-space(@class), ' '), 'rmx-ad'))]/div[1]/div[1]/div[1]/h3[1]/a[1]";
    private final String xpathDate = "//meta[contains(@itemprop,'datePublished')]";
    private final String xpathTitle = "//h1[contains(concat(' ', normalize-space(@class), ' '), 'headline')]";
    private final String xpathContent = "//div[contains(concat(' ', normalize-space(@itemtype), ' '), 'http://schema.org/Article')]";
    private final String xpathHtml = "//div[contains(concat(' ', normalize-space(@class), ' '), 'yog-content') or contains(concat(' ', normalize-space(@class), ' '), 'yog-col')]";
    //private final String xpathHtml = "//div[contains(concat(' ', normalize-space(@class), ' '), 'yog-content')]";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public ArrayList<String> getURLs() {
        driver.get(baseUrl);
        
        ArrayList<String> urls = new ArrayList<String>();
        
        List<WebElement> elements = driver.findElements(By.xpath(xpathUrl));
        Iterator<WebElement> i = elements.iterator();
        while(i.hasNext()) {
            WebElement element = i.next();
            urls.add(element.getAttribute("href"));
        }
        
        return urls;
    }
    
    public Article getPage(String url) throws UnreachableBrowserException {
        driver.get(url);
        
        Article info = new Article();
        info.setUrl(url);
        String datePublished = driver.findElement(By.xpath(xpathDate)).getAttribute("content");
        Calendar calendar = new GregorianCalendar();
        try {
			calendar.setTime(dateFormat.parse(datePublished));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        info.setDate(calendar.getTimeInMillis());
        info.setTitle(driver.findElement(By.xpath(xpathTitle)).getText());
        info.setText(driver.findElement(By.xpath(xpathContent)).getText());
//        WebElement htmlElement = driver.findElement(By.xpath(xpathHtml));
//        String htmlElementContent = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", htmlElement);
//        info.setHtml(htmlElementContent);
        
        return info;
    }
    
    public void quit() {
        driver.quit();
    }
    
    
}
