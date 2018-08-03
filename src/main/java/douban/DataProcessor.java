package douban;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 数据处理器，将数据持久化到MongoDB中
 */
public class DataProcessor<T> {

    private KafkaProducer kafkaProducer;

    public DataProcessor() {
        //kafkaProducer = initKafkaProducer();
    }

    private KafkaProducer initKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop1:9092,hadoop2:9092,hadoop3:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer(props);
    }

    public void process(List<T> results) {
        if (results == null || results.isEmpty()) {
            return;
        }

        try {

            // 数据
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("C:\\xhs_json.txt"), true)));
            Gson gson = new Gson();
            for (T result : results) {
                bw.write(gson.toJson(result));
                bw.write("\r\n");
            }
            bw.flush();
            bw.close();

            // 分词结果
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("C:\\xhs_word.txt"), true)));
            for (T result : results) {
                if (result instanceof Map) {
                    List<Word> words = WordSegmenter.seg(((Map) result).get("comment").toString());
                    pw.println(words.stream().map(word -> word.getText()).collect(Collectors.joining(" ")));
                }
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}