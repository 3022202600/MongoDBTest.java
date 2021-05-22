package clicks;

import clicks.model.ClicksLog;
import clicks.util.ConvertUtil;
import com.mongodb.MongoClient;
import goods.mongo.MongoDBUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MongoDBTest {
    private MongoClient mongoClient;

    @Before
    // 注解before 表示在方法前执行
    public void initMongoClient() throws IOException {
        mongoClient = MongoDBUtil.initMongo();
    }

    /*
     * 新增点击量日志
     * 创建数据库：ClicksLogTest，集合：clicksLog
     * */
    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    public void saveClicksLogTest() throws IllegalArgumentException,
            IllegalAccessException {
        Document document = ConvertUtil.convertDoc(this.initClicks());
        mongoClient.getDatabase("ClicksLogTest").getCollection("clicksLog")
                .insertOne(document);
    }

    /*
     * 查询统计点击量
     */
    @Test
    public void queryClicksLogTest() {
        long count = mongoClient.getDatabase("ClicksLogTest")
                .getCollection("clicksLog").count(new Document("clickPosition", "p_001"));
        //查询网页位置编号为“p_001”处的点击量统计数目
        System.out.println(count);
    }

    @After
    public void closeMongoClient() {
        mongoClient.close();
    }



    /*
     * 模拟一条点击网页记录内容
     * 供saveClicksLogTest() 调用，并存在数据库中
     * */
    private ClicksLog initClicks() {
        //初始化点击日志
        ClicksLog clicksLog = new ClicksLog();
        clicksLog.setClickPosition("p_001");
        clicksLog.setPageCode("page_001");
        clicksLog.setPageContent("广告页面");
        clicksLog.setUrl("http://test.ad.com");
        return clicksLog;
    }
}
