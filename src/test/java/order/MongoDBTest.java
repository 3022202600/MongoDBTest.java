package order;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;

import order.model.Goods;
import order.mongo.MongoDBUtil;

import order.util.ConvertUtil;
import order.model.Order;
import order.util.JsonUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;


public class MongoDBTest {//实现新增订单功能
    private MongoClient mongoClient;

    @Before
    // 注解before 表示在方法前执行
    public void initMongoClient() throws IOException {
        mongoClient = MongoDBUtil.initMongo();
    }

    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    public void saveOrderTest() throws IllegalArgumentException,
            IllegalAccessException {
        Document document = ConvertUtil.convertDoc(this.initOrder());
        mongoClient.getDatabase("OrderTest")
                .getCollection("Order").insertOne(document);
    }

    @Test
    //聚合查询订单数量
    public void queryOrderTest() {
        FindIterable<Document> iter = mongoClient
                .getDatabase("OrderTest").getCollection("Order")
                .find();
        System.out.println(iter.first().toJson());
    }

    @After
    //初始化订单信息
    public void closeMongoClient() {
        mongoClient.close();
    }
    private Order initOrder() {
        //商品销售记录
        Goods goods = new Goods();
        goods.setAdInfo("<html></html>");
        goods.setGoodsInfo("商品名称：华硕FX53VD商品编号：4380878商品毛重：4.19kg商品产地：中国大陆");
        goods.setId(4380878);
        goods.setSpecificationsInfo("主体系列飞行堡垒型号FX53VD颜色红黑平台Intel操作系统操作系统Windows 10家庭版处理器CPU类型Intel 第7代 酷睿CPU速度2.5GHz三级缓存6M其它说明I5-7300HQ芯片组芯片组其它　");
        goods.setNum(10);
        goods.setPrice(10);
        //订单总信息
        Order historyOrder = new Order();
        historyOrder.setCreatedDate(new Date());
        historyOrder.setGoodsList(JsonUtil.toJson(goods));
        historyOrder.setOrderId(3456712);
        historyOrder.setUserName("zhangsan");
        return historyOrder;
    }
}
