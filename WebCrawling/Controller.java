import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String crawlStorageFolder = "/data/crawl/testing"; 
		int numberOfCrawlers = 17; 
		
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder); 
		config.setPolitenessDelay(50);
		config.setMaxPagesToFetch(20000);
		config.setMaxDepthOfCrawling(16);
		config.setIncludeBinaryContentInCrawling(true);
		config.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
		PageFetcher pageFetcher = new PageFetcher(config);         
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); 
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);         
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		//List<String> crawler1Domains = ImmutableList.of("nytimes.com");
		controller.addSeed("https://www.latimes.com"); 
		//controller.addSeed("http://www.nytimes.com/"); 
		//controller.start(MyCrawler.class, numberOfCrawlers); 
		//CrawlController.WebCrawlerFactory<MyCrawler> factory1 = () -> new MyCrawler(crawler1Domains);
		/*controller.startNonBlocking(factory1, 17);
		controller.waitUntilFinish();*/
		controller.start(MyCrawler.class, numberOfCrawlers);
	}

}
