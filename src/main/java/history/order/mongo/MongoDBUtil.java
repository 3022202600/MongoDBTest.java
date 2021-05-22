package history.order.mongo;

import com.mongodb.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDBUtil {
    public static MongoClient initMongo() throws IOException {
        // 加载mongo配置文件
        InputStream inputStream = MongoDBUtil.class.getClass()
                .getResourceAsStream("/mongo-config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        WriteConcern concern = new WriteConcern(Integer.valueOf(properties
                .getProperty("write")), Integer.valueOf(properties
                .getProperty("writeTimeout")));
        concern.withJournal(Boolean.valueOf(properties.getProperty("journal")));
        MongoClientOptions.Builder builder = MongoClientOptions
                .builder()
                .connectionsPerHost(
                        Integer.valueOf(properties
                                .getProperty("connectionsPerHost")))
                .connectTimeout(
                        Integer.valueOf(properties
                                .getProperty("connectTimeout")))
                .cursorFinalizerEnabled(
                        Boolean.valueOf(properties
                                .getProperty("cursorFinalizerEnabled")))
                .maxWaitTime(
                        Integer.valueOf(properties.getProperty("maxWaitTime")))
                .threadsAllowedToBlockForConnectionMultiplier(
                        Integer.valueOf(properties
                                .getProperty("threadsAllowedToBlockForConnectionMultiplier")))
                .socketTimeout(
                        Integer.valueOf(properties.getProperty("socketTimeout")))
                .socketKeepAlive(
                        Boolean.valueOf(properties
                                .getProperty("socketKeepAlive")))
                .writeConcern(concern);
        if (Boolean.valueOf(properties.getProperty("readSecondary"))) {
            builder.readPreference(ReadPreference.secondaryPreferred());
        }
        String[] address = properties.getProperty("hostConfString").split(":");
        ServerAddress serverAddress = new ServerAddress(address[0],
                Integer.valueOf(address[1]));
        return new MongoClient(serverAddress, builder.build());
    }
}
