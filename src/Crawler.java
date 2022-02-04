package src;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYSeries;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final List<String> stopList = Arrays.asList("", "i", "the", "a", "an", "as", "at", "by", "he", "his", "me", "she", "her", "or", "us", "who", "and", "to", "be");
    private Map<String, List<Integer>> invertedIndex = new HashMap<String, List<Integer>>();
    private LineChart termChart;
    private XYSeries termsSearched = new XYSeries("Terms Searched");
    private LocalDateTime startTime = LocalDateTime.now();

    public List<String> crawl(String url, Integer id) {
        try {
            // fetch webpage html
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            // collect links on webpage
            Elements links = htmlDocument.select("a[href]");
            List<String> nextLinks = new ArrayList<String>();
            for (Element link : links) {
                nextLinks.add(link.absUrl("href"));
            }

            // get all words from body text
            String[] bodyText = htmlDocument.body().text().split(" ");
            List<String> words = new ArrayList<String>();
            for (String word : bodyText) {
                // remove links and stop list words
                word = word.toLowerCase();
                if (nextLinks.contains(word) || this.stopList.contains(word)) {
                    continue;
                }
                List<String> multWords = Arrays.asList(word.split("[,-/]"));
                for (String multWord : multWords) {
                    words.add(multWord.replaceAll("[^a-zA-Z]", ""));
                    /* STEMMER WORKS PRETTY BADLY RIGHT NOW
                    // remove punctuation and stem
                    Stemmer stemmer = new Stemmer();
                    String noPunc = multWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    stemmer.add(noPunc.toCharArray(), noPunc.length());
                    stemmer.stem();
                    words.add(stemmer.toString());'
                    */
                }
            }

            updateInvertedIndex(words, id);
            return nextLinks;
        } catch(Exception e) {
            System.out.println("An error occurred while crawling URL " + url + ": " + e);
            return new ArrayList<String>();
        }
    }

    private void updateInvertedIndex(List<String> words, Integer id) {
        for (String word : words) {
            if (invertedIndex.containsKey(word)) {
                List<Integer> docList = invertedIndex.get(word);
                int i = 0;
                for (Integer doc : docList) {
                    if (doc == id) break;
                    if (doc > id) {
                        docList.add(i, id);
                        break;
                    }
                    i++;
                    if (i == docList.size()) {
                        docList.add(i, id);
                        break;
                    }
                }
            } else {
                LinkedList<Integer> newList = new LinkedList<Integer>();
                newList.add(id);
                invertedIndex.put(word, newList);
            }
        }
        double minElapsed = (double)ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()) / 60;
        termsSearched.add(minElapsed, invertedIndex.size());
    }

    public void makeCharts() {
        this.termChart = new LineChart("WebCrawler Visualization", "Terms Seen over Time", "Elapsed Time (minutes)", "Terms Seen", Arrays.asList(termsSearched));
        termChart.pack();
        termChart.setVisible(true);
    }
}
