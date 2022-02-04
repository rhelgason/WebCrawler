package src;

public class WebCrawler {
    public static void main(String[] args) {
        System.out.println();
        // ENHANCEMENT: get max urls from args
        URLManager urlManager = new URLManager(1500, "./seed_urls.txt");
        urlManager.search();
    }
}