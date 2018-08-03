package douban;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网页解析器，解析网页返回链接列表和内容列表
 */
public class PageParser<T> {

    public static class Data<T> {

        private List<String> links;
        private List<T> results;

        public Data(List<String> links, List<T> results) {
            this.links = links;
            this.results = results;
        }

        public List<String> getLinks() {
            return links;
        }

        public void setLinks(List<String> links) {
            this.links = links;
        }

        public List<T> getResults() {
            return results;
        }

        public void setResults(List<T> results) {
            this.results = results;
        }
    }

    public Data<T> parse(String url, String html) {

        Document doc = Jsoup.parse(html, url);

        // 获取链接列表
        List<String> links =
                doc.select("#paginator > a.next")
                        .stream()
                        .map(a -> a.attr("abs:href"))
                        .collect(Collectors.toList());

        // 获取数据列表
        List<Map<String, Object>> results = doc.select("#comments > div.comment-item")
                .stream()
                .map(div -> {
                    Map<String, Object> data = new HashMap<>();

                    String author = div.selectFirst("h3 > span.comment-info > a").text();
                    String date = div.selectFirst("h3 > span.comment-info > span.comment-time").text();
                    Element rating = div.selectFirst("h3 > span.comment-info > span.rating");
                    String star = "0";
                    if (rating != null) {
                        // allstar40 rating
                        star = rating.attr("class");
                        star = star.substring(7, 9);
                    }
                    String vote = div.selectFirst("h3 > span.comment-vote > span.votes").text();
                    String comment = div.selectFirst("div.comment > p").text();

                    data.put("author", author);
                    data.put("date", date);
                    if (star != null)
                        data.put("star", star);
                    data.put("vote", vote);
                    data.put("comment", comment);

                    return data;
                })
                .collect(Collectors.toList());

        return new Data(links, results);
    }

}