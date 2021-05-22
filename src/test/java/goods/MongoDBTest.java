package goods;

import goods.util.ConvertUtil;
import com.mongodb.MongoClient;
import goods.model.Goods;
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

    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    //调用initGoods()方法，往数据库的集合里插入一条新的商品信息记录
    public void saveGoodsTest() throws IllegalArgumentException,
            IllegalAccessException {
        Document document = ConvertUtil.convertDoc(this.initGoods());
        mongoClient.getDatabase("Test").getCollection("goods")
                .insertOne(document);
    }

    @Test
    //对于输入出错的商品基本信息，可以通过如下代码实现原子性修改（实现了对ID为4380878的商品的内容的修改）
    public void queryGoodsTest() {
        Document doc = mongoClient.getDatabase("Test")
                .getCollection("goods")
                .findOneAndUpdate(new Document("id", 4380878), new Document("$set",new Document("adInfo", "<HTT>")));
        System.out.println(doc.toJson());
    }

    @After
    //关闭数据库连接
    public void closeMongoClient() {
        mongoClient.close();
    }


    // 初始化商品信息
    private Goods initGoods() {
        Goods goods = new Goods();
        goods.setAdInfo("<html></html>");
        goods.setGoodsInfo("商品名称：华硕FX53VD商品编号：4380878商品毛重：4.19kg商品产地：中国大陆");
        goods.setId(4380878);
        goods.setSpecificationsInfo("主体系列飞行堡垒型号FX53VD颜色红黑平台Intel操作系统操作系统Windows 10家庭版处理器CPU类型Intel 第7代 酷睿CPU速度2.5GHz三级缓存6M其它说明I5-7300HQ芯片组芯片组其它　");
        return goods;
    }

}
