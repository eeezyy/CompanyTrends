package aic13.group6.topic2.scrapper;

public class ScrapperData {
	private final static String baseUrl = "http://finance.yahoo.com/blogs/";
    private final static String xpathUrlNews = "//*/li[contains(concat(' ', normalize-space(@class), ' '), 'content') and not(contains(concat(' ', normalize-space(@class), ' '), 'rmx-ad'))]/div[1]/div[1]/div[1]/h3[1]/a[1]";
    private final static String xpathUrlSearch = "//div[@id='web']//a[contains(concat(' ', normalize-space(@class), ' '), 'yschttl') and contains(concat(' ', normalize-space(@class), ' '), 'spt')]";
    private final static String xpathDate = "string(//meta[contains(@itemprop,'datePublished')]/@content)";
    private final static String xpathTitle = "//h1[contains(concat(' ', normalize-space(@class), ' '), 'headline')]/text()";
    private final static String xpathContent = "//div[contains(concat(' ', normalize-space(@itemtype), ' '), 'http://schema.org/Article')]";
    private final static String xpathHtml = "//div[contains(concat(' ', normalize-space(@class), ' '), 'yog-content') or contains(concat(' ', normalize-space(@class), ' '), 'yog-col')]";
}
