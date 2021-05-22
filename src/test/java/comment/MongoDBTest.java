package comment;

import comment.util.ConvertUtil;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import comment.model.Comment;
import comment.model.CommentLabel;
import comment.util.JsonUtil;
import comment.mongo.MongoDBUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoDBTest { //开始建立一个MongoDB新增一个评价类
    private MongoClient mongoClient;

    @Before
    // 注解before 表示在方法前执行
    public void initMongoClient() throws IOException {
        mongoClient = MongoDBUtil.initMongo(); //实现与mongodb数据库的连接
    }

    @Test(timeout = 1000)
    // timeout表示该测试方法执行超过1s会抛出异常
    //
    public void saveCommentTest() throws IllegalArgumentException,
            IllegalAccessException {
        Document document = ConvertUtil.convertDoc(this.initComment()); //调用initComment()自定义函数，记录评价内容，转为文档格式
        mongoClient.getDatabase("CommentTest").getCollection("comment")
                .insertOne(document);  //把商品的一条评价文档记录插入CommentTest数据库的Comment集合中
    }

    @Test
    //查询功能
    public void queryCommentTest() {
        FindIterable<Document> iter = mongoClient.getDatabase("CommentTest")
                .getCollection("comment").find().skip(2).limit(2);  //调用find()方法，实现对comment集合数据的查找，
        // skip(2)为移动两条记录，从第三条开始获取评价记录。limit(2)为一次限制find()方法获取两条评价记录
        iter.forEach(new Block<Document>() { //通过FindIterable的forEach方法取得document信息
            public void apply(Document doc) {
                System.out.println(doc.toString());
            }
        });
    }

    @After
    public void closeMongoClient() {
        mongoClient.close();
    }
    private Comment initComment() {
        List<CommentLabel> commentLabels = new ArrayList<CommentLabel>();
        CommentLabel commentLabel1 = new CommentLabel();
        commentLabel1.setContent("商品不错");
        CommentLabel commentLabel2 = new CommentLabel();
        commentLabel2.setContent("非常耐用");
        commentLabels.add(commentLabel1);
        commentLabels.add(commentLabel2);
        Comment comment = new Comment();
        comment.setCommentLabels(JsonUtil.toJson(commentLabels));
        comment.setContent("快递非常快，下次还会买。");
        comment.setCreatedTime(new Date());
        comment.setPid(9000198);
        comment.setStar(4);
        comment.setUserName("admin");
        return comment;
    }
}
