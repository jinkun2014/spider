package douban;

import java.util.*;
import java.util.stream.Collectors;

/**
 * URL管理器，本工程中使用单线程，所以直接使用集合实现
 */
public class UrlManager {

    private String baseUrl;
    private Queue<String> newUrls = new LinkedList<>();
    private Set<String> oldUrls = new HashSet<>();

    public UrlManager(String baseUrl, String rootUrl) {
        this(baseUrl, Arrays.asList(rootUrl));
    }


    public UrlManager(String baseUrl, List<String> rootUrls) {
        if (baseUrl == null || rootUrls == null || rootUrls.isEmpty()) {
            return;
        }
        this.baseUrl = baseUrl;
        // 添加待抓取URL列表
        this.appendNewUrls(rootUrls);
    }

    /**
     * 追加待抓取URLs
     *
     * @param urls
     */
    public void appendNewUrls(List<String> urls) {
        // 添加待抓取URL列表
        newUrls.addAll(urls.stream()
                // 过滤指定URL
                .filter(url -> url.startsWith(baseUrl))
                // 处理URL中的多余参数(&status=P，有的链接有，有的没有，为避免重复，统一去除，去除后并不影响)
                .map(url -> url.replace("&status=P", ""))
                // 过滤重复的URL
                .filter(url -> !newUrls.contains(url) && !oldUrls.contains(url))
                // 返回处理过后的URL列表
                .collect(Collectors.toList()));
    }

    public boolean hasNewUrl() {
        return !this.newUrls.isEmpty();
    }

    /**
     * 取出一个新URL
     */
    public String getNewUrl() {
        String url = this.newUrls.poll();
        this.oldUrls.add(url);
        return url;
    }
}