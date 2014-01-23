package aic13.group6.topic2.scrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class YFinanceSearchUrlScrapperYQL {
    
	private final String yqlBaseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
	private final String validUrlPattern = "^http://finance.yahoo.com/(news|blogs).*";
	private final String urlPrefix = "http://alexander.kumbeiz.de/proxy/index.php?q=";
	private final String searchUrl = "http://alexander.kumbeiz.de/proxy/index.php?hl=20&q=finance.search.yahoo.com%2Fsearch%3Fp%3D";
	private final String query = "select * from html where url=\"#url\" and xpath=\"#xpath\"";
    private final String xpathUrl = "//div[@id='web']//a[contains(concat(' ', normalize-space(@class), ' '), 'yschttl') and contains(concat(' ', normalize-space(@class), ' '), 'spt')]";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public ArrayList<String> getArticleUrlsForCompany(String companyName) {
    	
    	ArrayList<String> urls = new ArrayList<String>();
    	
    	try {
			JSONObject object = getSearchResultAsJSON(companyName, xpathUrl);
			
			JSONArray uriJSONList = ((object.getJSONObject("query")).getJSONObject("results")).getJSONArray("a");

			for(int i = 0; i < uriJSONList.length(); i++) {
				String url = decodeUrl((uriJSONList.getJSONObject(i)).getString("href"));
				if(validateUrl(url)) {
					urls.add(url);
				}
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
    
    private JSONObject getSearchResultAsJSON(String searchTerm, String xpath) throws IOException, JSONException {
    	String buildQuery = query.replaceFirst("#url", searchUrl + URLEncoder.encode(searchTerm, "UTF-8")).replaceFirst("#xpath", xpath);
    	// decode characters (, ' and ) because YQL doesn't like it 
    	String buildQueryEncoded = ((URLEncoder.encode(buildQuery, "UTF-8").replaceAll("%28", "(")).replaceAll("%27", "'")).replaceAll("%29", ")");
		String encodedYqlUrlString = yqlBaseUrl + buildQueryEncoded + "&format=json";
		
		URL yqlUrl = new URL(encodedYqlUrlString);
		InputStream input = yqlUrl.openStream();
		
		JSONTokener tok = new JSONTokener(new InputStreamReader(input));
		JSONObject object = new JSONObject(tok);
    	return object;
    }
    
    private String decodeUrl(String url) {
    	url = url.substring(urlPrefix.length());
    	
    	String urlDecoded = null;
		try {
			urlDecoded = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return urlDecoded;
    }
    
    private boolean validateUrl(String url) {
    	Pattern p = Pattern.compile(validUrlPattern);
		Matcher m = p.matcher(url);
    	return m.matches();
    }
}
