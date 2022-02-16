# WebCrawler
A web crawler from scratch using jsoup to get webpage data. The web crawler starts from a few seed URLs and scans the Web in a breadth-first search manner. On each page, the web crawler collects all linked webpages to add them to the queue. All body text on the webpage is also collected to assemble an inverted index for which words appear in which webpages. This data structure would be useful for search querying in future updates.

## Architecture
### Seed URLs
To immediately penetrate the Web from a good starting point, we define a seed list of URLs for the crawler to begin with. The currently defined list of seed URLs includes only Wikipedia, for its large breadth of information, and cc.gatech.edu, for school relevance.

### URL Manager
The main functionality of the program stems from the URL Manager. This class maintains our visited set as well as our queue of upcoming webpages to scan. Together, these data structures allow us to scan the Web using a breadth-first search. In the event that an already visited webpage appears in the queue, our web crawler will recheck the page if it has been over an hour since the last scan on that page. This is to ensure up-to-date information for all crawled webpages.

### Inverted Index
The inverted index data structure uses a HashMap from strings to linked lists. Each string is a term from body text, and each linked list is a sorted list of documents IDs that contained the term. The data structure is visualized as follows:

<p align="center">
  <img src="https://github.com/rhelgason/WebCrawler/blob/main/img/inverted_index.PNG" alt="inverted index"/>
</p>

### Stop List
A small stop list is used to exclude the most common English words from skewing data. Although "the" is the most common English word, we can expect that this knowledge does not aid much to any given Web search. The complete stop list is as follows: "i", "in", "is", "for", "of", "the", "a", "an", "as", "at", "by", "he", "his", "me", "she", "her", "or", "us", "who", "and", "to", and "be".

### Stemmer
A simple stemmer is included with the program to reduce words to their roots (ex: acceleration -> accelerate). The logic for this stemmer is very archaic as of yet, so it does not add much to the web crawler. Future iterations will hope to improve this.

## Runtime
The web crawler does not outwardly display too much during runtime. The command line displays the current count of URLs crawled, as well as the URL currently being crawled. Any time a webpage is recrawled because it has been over an hour since its last scan, this is noted as well. Finally, any errors that occur while crawling are also displayed. These errors usually come from faulty link tags on webpages. Sample output is shown here:

<p align="center">
  <img src="https://github.com/rhelgason/WebCrawler/blob/main/img/sample_command_line.PNG" alt="inverted index"/>
</p>

Once the web crawler has finished crawling the specified number of URLs, it concludes by printing term frequency information and relevant line graphs. These are all detailed in the following section.

## Findings
Considering the time complexity of running this web crawler on relatively limited hardware, I was able to produce a web crawling of 1,500 unique URLs. This took a little over two hours on my machine. The following is a visualization of URLs searched over time (note that this does not consider recrawled webpages):

<p align="center">
  <img src="https://github.com/rhelgason/WebCrawler/blob/main/img/url_search_1500.PNG" alt="inverted index"/>
</p>

Another data point that was tracked was the ratio of URLs crawled to unique URLs waiting in queue to be crawled. This ratio rises rapidly within the first ~10 minutes, but levels out for the remainder of the program's runtime. This suggests that a lot of loops were present in the linking structure. This doesn't come as too much of a surprise because one of the two seed URLs was Wikipedia, and most of the links present on any given Wikipedia page will simply link to other Wikipedia pages. The line graph is visualized here:

<p align="center">
  <img src="https://github.com/rhelgason/WebCrawler/blob/main/img/url_ratio_1500.PNG" alt="inverted index"/>
</p>

To little surprise, the number of unique terms seen over time rises sharply at the onset of the program before slowly decelerating. Every unique term found slows down the web crawler greatly because all inverted index operations take longer to complete. This is the reason for the declining pace in the URLs searched over time chart. In the future, a better stop list and stemmer will help the runtime. The graph of unique terms over time is visualized here:

<p align="center">
  <img src="https://github.com/rhelgason/WebCrawler/blob/main/img/terms_seen_1500.PNG" alt="inverted index"/>
</p>

Finally, the web crawler compiles a list of the words that appear in the most documents. From the seed URLs mentioned above, the following words appear in the most documents:
1. "privacy" appeared in 946 webpages
2. "search" appeared in 946 webpages
3. "about" appeared in 938 webpages
4. "on" appeared in 937 webpages
5. "information" appeared in 931 webpages
6. "not" appeared in 927 webpages
7. "navigation" appeared in 926 webpages
8. "main" appeared in 919 webpages
9. "contact" appeared in 919 webpages
10. "community" appeared in 917 webpages

A perceptive reader will probably recognize that this list is a bit odd to be the most frequently appearing terms in a web crawl. Wikipedia represented the overarching majority of the web crawl because of its presence in the seed URL list and tendency to link mostly to itself. Future iterations will hope to account for this and improve the seed URL list.

## Gained Knowledge and Predictions
Before beginning this project, I already had a lot of interest in creating a web crawler. To my surprise, the basic data structures surrounding the web crawler were easier to flesh out than I expected. By far, the greater issue is improving the efficiency of the crawler. As mentioned, I still have a lot of improvements to make in this regard, but it is not too bad right now. The data structures, stop list, and stemmer all serve to improve the runtime of the crawler.

Although outside the bounds of this assignment, I am interested to perform some querying on the inverted index my crawler has developed. Truly, the web crawler is only part of the search engine issue. I would be interested to further expand what I have developed so far.

My web crawler takes roughly 1 hour and 5 minutes to scan 1,500 links. Expanding the web crawler to do more links than this produces a dismal outlook. Assuming logarithmic regression, scanning 10 million unique pages would take almost a year to complete. Even worse, one billion webpages would take over 90 years to complete. The only solutions to this issue would be improving the crawler's efficiency and getting much more computing power.

## Dependencies
Although the associated .jar files have not been included in the repository, the following dependencies were used to produce the crawler's current output:
- commons-collections4-4.4
- commons-io-2.11.0
- jfreechart-demo-1.5.2-jar-with-dependencies
- jsoup-1.14.3
- poi-5.2.0
- poi-ooxml-5.2.0
- xmlbeans-5.0.3
