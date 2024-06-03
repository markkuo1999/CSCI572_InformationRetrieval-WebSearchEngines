import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

//import java.net.HttpURLConnection;
import java.net.URL;

import com.google.common.collect.ImmutableList;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	
	//private final static Pattern FILTERS = Pattern.compile(
			//".*\\.(woff2|woff|xml|rss|iso|zip|rar|gz|exe|css|js|json|webmanifest|ttf|svg|wav|avi|mov|mpeg|mpg|ram|m4v|wma|wmv|mid|txt|mp2|mp3|mp4)");
	
	String[] extensions = {"woff2", "woff", "xml", "rss", "iso", "zip", "rar", "gz", "exe", "css", "js", "json", "webmanifest", "ttf", "svg", "wav", "avi", "mov", "mpeg", "mpg", "ram", "m4v", "wma", "wmv", "mid", "txt", "mp2", "mp3", "mp4"};
	

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        String href = webUrl.getURL();
        href = href.replace(",", "-");
        if ((href.startsWith("http://www.latimes.com") || href.startsWith("http://latimes.com")
        		|| href.startsWith("https://www.latimes.com") || href.startsWith("https://latimes.com"))) {
        	
        	boolean containsCSSOrOtherExtension = false;
        	for (String extension : extensions) {
        	    if (href.contains("." + extension)) {
        	        containsCSSOrOtherExtension = true;
        	        break;
        	    }
        	}
        	
        	if(containsCSSOrOtherExtension == false) {
        		try (FileWriter writer = new FileWriter("/data/crawl/testing/fetch_latimes.csv", true)) {
    	            writer.append(href);
    	            writer.append(",");         
    	            writer.append(String.valueOf(statusCode));     
    	            System.out.println("Fetched Status Code: " + statusCode);
    	            writer.append("\n");
    	            writer.flush();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
        	}
			
        }
        
    }
    
	@Override      
	public boolean shouldVisit(Page referringPage, WebURL url) { 
		//String href = url.getURL().toLowerCase();    
		String href = url.getURL();  
		href = href.replace(",", "-");
		//return FILTERS.matcher(href).matches()                 
		//&& href.startsWith("https://www.latimes.com/"); 
		//String URL = referringPage.getWebURL().getURL(); 
		//String initialhost = "latimes.com";
		try (FileWriter writer3 = new FileWriter("/data/crawl/testing/urls_latimes.csv", true)) {
			writer3.append(href);
            writer3.append(",");
            //System.out.println("host: " + page.getWebURL().getDomain()); 
            //initialhost = "latimes.com";
            if ((href.startsWith("http://www.latimes.com") || href.startsWith("http://latimes.com")
            		|| href.startsWith("https://www.latimes.com") || href.startsWith("https://latimes.com"))) 
            	writer3.append("OK");
            else
            	writer3.append("N_OK");
                
            writer3.append(",");
            writer3.append("\n");
            writer3.flush();
            
			
        } catch (IOException e) {
            e.printStackTrace();
        }
	
		//System.out.println("statusCode: " + statusCode); 
		if ((href.startsWith("http://www.latimes.com") || href.startsWith("http://latimes.com")
        		|| href.startsWith("https://www.latimes.com") || href.startsWith("https://latimes.com"))) {
        	for (String extension : extensions) {
        	    if (href.contains("." + extension)) {
        	    	return false;      	   
        	    }
        	}
			return true;
		}

		return false;
	}
	
	@Override      
	public void visit(Page page) { 
		String url = page.getWebURL().getURL(); 
		url = url.replace(",", "-");
		String initialhost = "latimes.com";
		//System.out.println("URL: " + url); 
		int statusCode = page.getStatusCode();
		String contentType = page.getContentType();
		
		try (FileWriter writer2 = new FileWriter("/data/crawl/testing/visit_latimes.csv", true)) {
			writer2.append(url);
	        writer2.append(",");
	        writer2.append(String.valueOf(page.getContentData().length)); // File size in bytes
	        writer2.append(",");
	        writer2.append(String.valueOf(page.getParseData().getOutgoingUrls().size())); // Number of outgoing links
	        writer2.append(",");
	        writer2.append(page.getContentType().split(";")[0]); // Content type
	        writer2.append("\n");
	        writer2.flush();
			
        } catch (IOException e) {
            e.printStackTrace();
        }
	
		//System.out.println("Status Code: " + statusCode);
		if (page.getParseData() instanceof HtmlParseData) { 
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();              
			String text = htmlParseData.getText();              
			String html = htmlParseData.getHtml(); 
			Set<WebURL> links = htmlParseData.getOutgoingUrls();               
			System.out.println("Text length: " + text.length());              
			System.out.println("Html length: " + html.length()); 
			System.out.println("Number of outgoing links: " + links.size()); 
		}
	}
}
