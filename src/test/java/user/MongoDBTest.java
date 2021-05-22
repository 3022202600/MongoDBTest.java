package user;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import user.mongo.MongoDBUtil;
import user.model.UserExtendInfo;
import user.model.UserInfo;
import user.util.ConvertUtil;
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

    //以下为登记信息的插入库过程
    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    public void saveUserTest() throws IllegalArgumentException,
            IllegalAccessException {
        Document userInfoDoc = ConvertUtil.convertDoc(this.initUserInfo());
        Document userBaseDoc = ConvertUtil.convertDoc(
                this.initUserExtendInfo(), userInfoDoc);
        mongoClient.getDatabase("UserTest").getCollection("user")
                .insertOne(userBaseDoc);
    }

    @Test
    //多条件查询用户
    public void queryUserTest() {
        FindIterable<Document> findIterable = mongoClient
                .getDatabase("UserTest").getCollection("user")
                .find(new Document("age", 20).append("name", "张三"));
        System.out.println(findIterable.first().toString());
    }
    //根据查询条件：年龄为20、姓名为张三获取用户基本信息和扩展信息。
    //然后把获取相关信息，转为特定广告信息获取的查询参数，最终获取特定的广告内容

    @After
    public void closeMongoClient() {
        mongoClient.close();
    }

    /*下列代码分别实现了用户基本信息记录、用户扩展信息记录过程。
              用户基本信息*/
    private UserInfo initUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(20);
        userInfo.setName("张三");
        userInfo.setNickName("小明");
        userInfo.setSex("男");
        userInfo.setUserName("zhangsan");
        return userInfo;
    }


    /*
     * 用户扩展信息
     * */
    private UserExtendInfo initUserExtendInfo() {
        UserExtendInfo userExtendInfo = new UserExtendInfo();
        userExtendInfo.setAddress("中国北京");
        userExtendInfo.setCodeNum("123456789012345678");
        userExtendInfo.setPhone("13000012121");
        userExtendInfo.setSchool("清华大学");
        return userExtendInfo;
    }
}
