import okhttp3.*;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.io.IOException;
import java.util.List;

/**
 * Description: HelloWorld！ <br/>
 * Autor: Created by PCuser on 2018/6/22.
 */
public class Test {
    public static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postForm(String url) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("username", "android")
                .add("password", "bug")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postJson(String url) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"123\"}");

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws Exception {
        String line = "打扰了……每个剧情的转折都很尬。前面配乐少的可怜，感觉生硬点在演，而且还像是棚拍的。喜欢那首欢快的歌。";
        List<Word> words = WordSegmenter.seg(line);
        System.out.println(words);
    }
}
