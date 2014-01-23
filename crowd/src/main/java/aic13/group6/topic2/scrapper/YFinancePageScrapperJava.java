package aic13.group6.topic2.scrapper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;














import org.xml.sax.SAXException;

import aic13.group6.topic2.entities.Article;

public class YFinancePageScrapperJava {

	public Article getPage(String url) {
		
		Document document = null;
		try {
			URL urlObject = new URL(url);
			StringWriter writer = new StringWriter();
			IOUtils.copy(urlObject.openStream(), writer, "UTF-8");
			TagNode tagNode = new HtmlCleaner().clean(writer.toString());
			
		    document = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		Article article = new Article();
		
		try {
			String datePublished = xPath.compile(ScrapperData.xpathDate).evaluate(document);
			String title = xPath.compile(ScrapperData.xpathTitle).evaluate(document);
			
			// remove script node so its text will not appear
			NodeList nodeList = (NodeList) xPath.compile(ScrapperData.xpathContent).evaluate(document, XPathConstants.NODE);
			for(int i = 0; i < nodeList.getLength(); i++) {
				removeNode(nodeList.item(i), "script");
			}
			String text = xPath.compile(ScrapperData.xpathContent).evaluate(document);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Calendar calendar = new GregorianCalendar();
	        try {
				calendar.setTime(dateFormat.parse(datePublished));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        article.setUrl(url);
			article.setDate(calendar.getTimeInMillis());
			article.setTitle(title);
			article.setText(text.trim());
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return article;
	}
	
	private void removeNode(Node node, String name) {
		NodeList childs = node.getChildNodes(); 
		for(int i = 0; i < childs.getLength(); i++) {
			if(childs.item(i).getNodeName().equals(name)) {
				childs.item(i).getParentNode().removeChild(childs.item(i));
			}
			else if(childs.item(i).hasChildNodes()) {
				NodeList childChilds = childs.item(i).getChildNodes();
				for(int j = 0; j < childChilds.getLength(); j++) {
					removeNode(childChilds.item(j), name);
				}
			}
		}
	}
}
