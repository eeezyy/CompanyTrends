package aic13.group6.topic2.scrapper;

public class ScrapperData {
	
	// YQL
	public final static String yqlBaseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
	public final static String query = "select * from html where url=\"#url\" and xpath=\"#xpath\"";
	
	// Blog
	public final static String pageBaseUrl = "http://finance.yahoo.com/blogs/";
	
	// XPath
	public final static String xpathUrlNews = "//div[contains(concat(' ', normalize-space(@class), ' '), 'type_stream')]/*//a";
	public final static String xpathUrlSearch = "//div[@id='web']//a[contains(concat(' ', normalize-space(@class), ' '), 'yschttl') and contains(concat(' ', normalize-space(@class), ' '), 'spt')]";
	public final static String xpathDate = "string(//meta[contains(@itemprop,'datePublished')]/@content)";
	public final static String xpathTitle = "//h1[contains(concat(' ', normalize-space(@class), ' '), 'headline')]/text()";
	public final static String xpathContent = "//div[contains(concat(' ', normalize-space(@itemtype), ' '), 'http://schema.org/Article')]";
	public final static String xpathHtml = "//div[contains(concat(' ', normalize-space(@class), ' '), 'yog-content') or contains(concat(' ', normalize-space(@class), ' '), 'yog-col')]";
}
