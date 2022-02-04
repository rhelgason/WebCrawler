package src;

import java.util.Set;

import org.jfree.data.xy.XYSeries;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class URLManager {
    private static final int RECHECK_HOURS = 1;
    private LocalDateTime startTime;
    private int numRemaining;
    private int maxUrls;
    // translate document id to url
    private Map<Integer, String> docTranslator;
    // translate url to document id and last checked time
    private Map<String, List<Object>> urlTranslator;
    private Set<String> visited;
    private List<String> queue;
    private Crawler crawler;
    private LineChart searchedChart;
    private LineChart ratioChart;

    public URLManager(int maxUrls, String seedPath) {
        this.maxUrls = maxUrls;
        this.startTime = LocalDateTime.now();
        this.numRemaining = 0;
        this.docTranslator = new HashMap<Integer, String>();
        this.urlTranslator = new HashMap<String, List<Object>>();
        this.visited = new HashSet<String>();
        this.queue = new LinkedList<String>();
        addSeedUrls(seedPath);
        this.crawler = new Crawler();
    }

    public void search() {
        final XYSeries urlSearched = new XYSeries("URLs Searched");
        final XYSeries urlRatio = new XYSeries("Ratio of URLs crawled to URLs remaining");

        while(queue.size() != 0 && this.visited.size() < maxUrls) {
            String curr = this.nextUrl();
            System.out.println("Crawling site " + (this.visited.size() + 1) + " at URL " + curr);
            List<String> nextLinks = crawler.crawl(curr, this.visited.size() + 1);
            if (nextLinks.size() != 0) {
                this.docTranslator.put(this.visited.size() + 1, curr);
                this.urlTranslator.put(curr, Arrays.asList(this.visited.size() + 1, LocalDateTime.now()));
                this.visited.add(curr);
                for (String link : nextLinks) {
                    if (!this.visited.contains(link) && !this.queue.contains(link)) numRemaining += 1;
                }
                this.queue.addAll(nextLinks);
                double minElapsed = (double)ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()) / 60;
                urlSearched.add(minElapsed, this.visited.size());
                urlRatio.add(minElapsed, (float)this.visited.size() / numRemaining);
            }
        }
        this.makeCharts(urlSearched, urlRatio);
    }

    private void addSeedUrls(String seedPath) {
        try {
            Scanner scanner = new Scanner(new File(seedPath));
            while (scanner.hasNextLine()) {
                queue.add(scanner.nextLine());
                this.numRemaining += 1;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Unable to add seed URLs to queue.");
            System.exit(0);
        }
    }

    // get the next unvisited url to crawl
    private String nextUrl() {
        String next = this.queue.remove(0);
        while (this.visited.contains(next)) {
            if (true) {
                this.recheckUrl(next);
            }
            next = this.queue.remove(0);
        }
        return next;
    }

    // recheck a webpage for updates
    private void recheckUrl(String url) {
        Integer id = (Integer) urlTranslator.get(url).get(0);
        LocalDateTime lastCheck = (LocalDateTime) urlTranslator.get(url).get(1);
        if (ChronoUnit.HOURS.between(lastCheck, LocalDateTime.now()) < RECHECK_HOURS) return;
        System.out.println("Recrawling site " + id + " at URL " + url);
        List<String> nextLinks = crawler.crawl(url, id);
        if (nextLinks.size() != 0) this.queue.addAll(nextLinks);
    }

    // create charts for URLs and terms
    private void makeCharts(XYSeries urlSearched, XYSeries urlRatio) {
        this.searchedChart = new LineChart("WebCrawler Visualization", "URLs Searched over Time", "Elapsed Time (minutes)", "URLs Searched", Arrays.asList(urlSearched));
        searchedChart.pack();
        searchedChart.setVisible(true);

        this.ratioChart = new LineChart("WebCrawler Visualization", "Ratio of URLs crawled to URLs remaining", "Elapsed Time (minutes)", "Ratio", Arrays.asList(urlRatio));
        ratioChart.pack();
        ratioChart.setVisible(true);

        crawler.makeCharts();
    }
}