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
public class YFinancePageScrapperYQL {
    
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private JSONObject getJSONObject(String urlPath, String xpath) throws IOException, JSONException {
    	String buildQuery = ScrapperData.query.replaceFirst("#url", urlPath).replaceFirst("#xpath", xpath);
    	String buildQueryEncoded = ((URLEncoder.encode(buildQuery, "UTF-8").replaceAll("%28", "(")).replaceAll("%27", "'")).replaceAll("%29", ")");
    	String encodedYqlUrlString = ScrapperData.yqlBaseUrl + buildQueryEncoded + "&format=json";
    	System.out.println(encodedYqlUrlString);
    	URL yqlUrl = new URL(encodedYqlUrlString);
		InputStream input = yqlUrl.openStream();
		
		JSONTokener tok = new JSONTokener(new InputStreamReader(input));
		JSONObject object = new JSONObject(tok);
    	return object;
    }
    
    public Article getPage(String url) {
        
        Article info = new Article();
        info.setUrl(url);
        
        try {
			JSONObject objectDate = getJSONObject(url, ScrapperData.xpathDate).getJSONObject("query");
			JSONObject objectTitle = getJSONObject(url, ScrapperData.xpathTitle).getJSONObject("query");
			JSONObject objectContent = getJSONObject(url, ScrapperData.xpathContent).getJSONObject("query");
//			JSONObject objectHtml = getJSONObject(url, ScrapperData.xpathHtml).getJSONObject("query");

			System.out.println(objectDate);
			System.out.println(objectTitle);
			System.out.println(objectContent);
//			System.out.println(objectCHtml);
			
			String datePublished = objectDate.getString("results");
	        Calendar calendar = new GregorianCalendar();
	        try {
				calendar.setTime(dateFormat.parse(datePublished));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        String title = objectTitle.getString("results");
	        String text = objectTitle.getString("results");
//	        String html = objectTitle.getString("results");
	        
	        info.setDate(calendar.getTimeInMillis());
	        info.setTitle(null);
	        info.setText(null);
        } catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
        
        return info;
    }
    
    
}
