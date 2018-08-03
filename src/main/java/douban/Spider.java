package douban;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 豆瓣电影影评爬虫，本爬虫是一个单线程爬虫
 * 参考：https://www.jianshu.com/p/bbbe5f41b8bd
 */
public class Spider {

    private UrlManager urlManager;
    private Downloader downloader;
    private PageParser pageParser;
    private DataProcessor dataProcessor;

    public Spider(UrlManager urlManager, Downloader downloader, PageParser pageParser, DataProcessor dataProcessor) {
        this.urlManager = urlManager;
        this.downloader = downloader;
        this.pageParser = pageParser;
        this.dataProcessor = dataProcessor;
    }

    static class Builder {
        private UrlManager urlManager;
        private Downloader downloader;
        private PageParser pageParser;
        private DataProcessor dataProcessor;

        public Builder() {
        }

        public Builder setUrlManager(UrlManager urlManager) {
            this.urlManager = urlManager;
            return this;
        }

        public Builder setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public Builder setPageParser(PageParser pageParser) {
            this.pageParser = pageParser;
            return this;
        }

        public Builder setDataProcessor(DataProcessor dataProcessor) {
            this.dataProcessor = dataProcessor;
            return this;
        }

        public Spider build() {
            return new Spider(urlManager, downloader, pageParser, dataProcessor);
        }
    }

    /**
     * 启动爬虫，任务执行完成后，返回处理URL数量
     *
     * @return
     */
    private long start() {
        final AtomicLong counter = new AtomicLong();
        while (urlManager.hasNewUrl()) {
            try {
                String url = urlManager.getNewUrl();
                if (url == null) break;
                String html = downloader.download(url);
                PageParser.Data data = pageParser.parse(url, html);
                if (data == null) continue;
                if (data.getLinks() != null) {
                    urlManager.appendNewUrls(data.getLinks());
                }
                if (data.getResults() != null) {
                    dataProcessor.process(data.getResults());
                }
                counter.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return counter.get();
    }

    public static void main(String[] args) {

        // 豆瓣影评URL部分不变，变化的只有参数部分
        String BASE_URL = "https://movie.douban.com/subject/27605698/comments";
        String ROOT_URL = BASE_URL + "?start=0&limit=20&sort=new_score&status=P";

        // 构建爬虫并启动爬虫，这里仅作最小化演示，程序健壮性、扩展性等暂不考虑
        Spider spider = new Spider.Builder()
                .setUrlManager(new UrlManager(BASE_URL, ROOT_URL))
                .setDownloader(new Downloader())
                .setPageParser(new PageParser())
                .setDataProcessor(new DataProcessor())
                .build();
        long urls = spider.start();
        System.out.println("任务执行完成，共爬取 " + urls + " 个URL");
    }
}