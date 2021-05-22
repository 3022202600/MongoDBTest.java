package log;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import log.mongo.MongoDBUtil;
import log.util.ConvertUtil;
import log.model.Log;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class MongoDBTest {
    //建立mongoClient类私有实体对象
    private MongoClient mongoClient;  //建立mongoClient类私有实体对象

    @Before
    // 注解before 表示在方法前执行
    public void initMongoClient() throws IOException {
        mongoClient = MongoDBUtil.initMongo();
    }

    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    //保存日志方法，实现一条文档插入集合过程
    public void saveLogTest() throws IllegalArgumentException,
            IllegalAccessException {
        mongoClient.getDatabase("LogTest").getCollection("log")
                .insertOne(ConvertUtil.convertDoc(this.initLog()));
    }//连接Log数据库的log集合，并插入一条日志文档


    @Test
    public void queryLogTest() {
        //调用find()方法，实现对log集合数据的查找
        FindIterable<Document> iter = mongoClient.getDatabase("LogTest")
                .getCollection("test").find();
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                String result = doc.toString();
                System.out.println(result);
//                System.out.println(doc.toJson());
            }
        });
    }

    @After
    public void closeMongoClient() {
        mongoClient.close();
    }
    private Log initLog() {    //产生一条模拟服务器记录日志
        Exception e = new NullPointerException("-------Test-------");
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        Log log = new Log();     //建立log实体对象
        log.setCreatedTime(new Date());
        log.setLevel("ERROR");
        log.setMessage(e.getMessage());    //得到代码指向出错的相关信息
        log.setStackTrace(sw.getBuffer().toString());
        return log;   //返回产生的日志集合对象的文档内容
    }
}
