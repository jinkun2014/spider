package douban;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP下载器，下载网页和其它资源文件
 */
public class Downloader {

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.MILLISECONDS)
            .build();

    /**
     * 下载网页
     *
     * @param url
     * @return
     */
    public String download(String url) {
        System.out.println("url:" + url);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 使用Cookie消息头是为了简化登录问题(豆瓣电影评论不登录条件下获取不到全部数据)
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .addHeader("Cookie", "bid=JpzLF8IY6ME; ps=y; ue=\"389993091@qq.com\"; dbcl2=\"65061362:jPkwMH9sChw\"; ck=BqDN; ap=1; _pk_id.100001.4cf6=753ba2b32a41b881.1533103095.2.1533109232.1533103103.; _pk_ses.100001.4cf6=*; __utma=223695111.2111105877.1533103095.1533103095.1533108693.2; __utmb=223695111.0.10.1533108693; __utmc=223695111; __utmz=223695111.1533103095.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); push_noty_num=0; push_doumail_num=0; __utma=30149280.1388652800.1533103095.1533103095.1533108693.2; __utmb=30149280.0.10.1533108693; __utmc=30149280; __utmz=30149280.1533103095.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException(response.code() + "," + response.message());
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}