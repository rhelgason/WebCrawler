default: src/WebCrawler.java src/URLManager.java src/Crawler.java
	javac src/WebCrawler.java src/URLManager.java src/Crawler.java

run: src/WebCrawler.class src/URLManager.class src/Crawler.class
	java src.WebCrawler

clean:
	rm -rf src/*.class