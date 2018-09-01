package dango.mongodb;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * @author: DANGO
 * @date 2018/8/30 9:32
 * @Description:
 */
public class MongoUtil {

    private static MongoClient mongoClient;

    static {
        System.out.println("MongoDB 初始化");
        String ip = "127.0.0.1";
        int port = 27017;
        mongoClient = new MongoClient(ip, port);
        Builder builder = new MongoClientOptions.Builder();
        builder.cursorFinalizerEnabled(true);
        builder.connectionsPerHost(300);
        builder.connectTimeout(30000);
        builder.maxWaitTime(5000);
        builder.socketTimeout(0);
        builder.threadsAllowedToBlockForConnectionMultiplier(5000);
        builder.writeConcern(WriteConcern.SAFE);
        builder.build();
    }

    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    public MongoCollection<Document> getCollection(String dbName, String collectionName) {
        if (collectionName == null || "".equals(collectionName) || dbName == null || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = getDB(dbName).getCollection(collectionName);
        return collection;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
        System.out.println("\nMongoDB 关闭连接");
    }

    public static void main(String[] args){
        MongoUtil mongoUtil=new MongoUtil();
        String dbName="test";
        String collectionName="runoob";
        MongoDatabase testDatabase=mongoUtil.getDB(dbName);
        MongoCollection<Document> runoobCollection=testDatabase.getCollection(collectionName);
        MongoIterable<String> iterable;

        System.out.println("\n获取所有数据库名称列表");
        iterable=mongoClient.listDatabaseNames();
        for (String s:iterable){
            System.out.println(s);
        }

        System.out.println("\n查询 test 数据库所有集合名（表名）");
        iterable=testDatabase.listCollectionNames();
        for (String s:iterable){
            System.out.println(s);
        }

        System.out.println("\n查询 test 数据库下 runoob 集合文档数");
        System.out.println(runoobCollection.count());
        System.out.println("\n查询 test 数据库下 dango 集合文档数");
        System.out.println(mongoUtil.getCollection("test","dango").count());

//        System.out.println("\n 插入数据");
//        Document document=new Document();
//        document.put("x","dango");
//        document.put("lala","zelda");
//        Document document2=new Document();
//        document2.put("lucifer","一碗鱼子酱");
//        document2.put("name","尉迟晚临江");
//        document.put("document_test",document2);
//        runoobCollection.insertOne(document);
//        System.out.println("插入:"+document);

        MongoCursor<Document> cursor;
        System.out.println("\n查询 test 数据库下 runoob 集合中所有文档");
        cursor=runoobCollection.find().iterator();
        while (cursor.hasNext()){
            Document d=cursor.next();
            System.out.println(d.toString());
            System.out.println("x--->"+d.get("x"));
        }


        System.out.println("\n 根据条件查询(x= dango )");
        Bson bson =Filters.eq("x","dango");
        cursor=runoobCollection.find(bson).iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }
        System.out.println("\n 根据条件查询(x >= 100 )");
        Bson bson2=Filters.gte("x",100);
        cursor=runoobCollection.find(bson2).iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }
        System.out.println("\n 根据条件查询(x < 100 )");
        Bson bson3=Filters.lt("x",100);
        cursor=runoobCollection.find(bson3).iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }

        System.out.println("\n 多条件组合查询");

        cursor=runoobCollection.find(bson3).iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }



        System.out.println("\n 修改数据");
        cursor=runoobCollection.find(bson).iterator();
        while (cursor.hasNext()){
            Document d=cursor.next();
            Bson filter=Filters.eq("_id",d.get("_id"));
            d.put("lala","update_5");
            System.out.println(runoobCollection.replaceOne(filter,d));
        }

        System.out.println("\n 删除数据");
        Bson filter=Filters.eq("_id",new ObjectId("5b8756082d11e304f4236b3f"));
        System.out.println(runoobCollection.deleteOne(filter));

        System.out.println("\n查询 test 数据库下 runoob 集合中所有文档");
        cursor=runoobCollection.find().iterator();
        while (cursor.hasNext()){
            Document d=cursor.next();
            System.out.println(d.toString());
        }

        mongoUtil.close();
    }

}
