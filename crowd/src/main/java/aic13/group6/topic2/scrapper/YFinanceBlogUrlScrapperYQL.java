package aic13.group6.topic2.scrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.sun.jersey.api.uri.UriBuilderImpl;
import com.sun.jersey.api.uri.UriTemplate;

import aic13.group6.topic2.entities.Article;

/**
 *	Scrappes the finance news article issued by Yahoo!
 */
public class YFinanceBlogUrlScrapperYQL {
    
	private final String yqlBaseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
	private final String pageBaseUrl = "http://finance.yahoo.com/blogs/";
	private final String query = "select * from html where url=\"#url\" and xpath=\"#xpath\"";
    private final String xpathUrl = "//*/li[contains(concat(' ', normalize-space(@class), ' '), 'content') and not(contains(concat(' ', normalize-space(@class), ' '), 'rmx-ad'))]/div[1]/div[1]/div[1]/h3[1]/a[1]";
    
    public ArrayList<String> getURLs() {
    	
    	ArrayList<String> urls = new ArrayList<String>();
    	
    	try {
			String buildQuery = query.replaceFirst("#url", pageBaseUrl).replaceFirst("#xpath", xpathUrl);
			String encodedYqlUrlString = yqlBaseUrl + URLEncoder.encode(buildQuery, "UTF-8") + "&format=json";
			URL yqlUrl = new URL(encodedYqlUrlString);
			InputStream input = yqlUrl.openStream();
			
			JSONTokener tok = new JSONTokener(new InputStreamReader(input));
			JSONObject object = new JSONObject(tok);
			JSONArray uriJSONList = ((object.getJSONObject("query")).getJSONObject("results")).getJSONArray("a");

			for(int i = 0; i < uriJSONList.length(); i++) {
				String url = (uriJSONList.getJSONObject(i)).getString("href");
				urls.add(url);
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return urls;
    }
    
}
