package sample;

import java.net.UnknownHostException;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Main {

    private static void bulkInsert(DBCollection col) {

        BulkWriteOperation bulk = col.initializeUnorderedBulkOperation();

        for (int i = 0; i < 100; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("counter", i);
            // lets just use UUID randomUUID - keep it simple
            String msg = UUID.randomUUID().toString();
            obj.put("msg", msg);
            bulk.insert(obj);
        }

        BulkWriteResult res = bulk.execute();
        System.out.println("Bulk Insert");
        System.out.println(res);

    }

    public static void main(String[] args) {

        try {
            MongoClient mc = new MongoClient();

            DB db = mc.getDB("java");
            db.dropDatabase();
            DBCollection col = db.getCollection("sample");

            // bulkInsert(col);
            
            OplogViewer viewer = new OplogViewer(mc);

            Thread t = new Thread(viewer);
            t.start();

            OperatorsOplog operator = new OperatorsOplog(col);
            operator.applySet();
            operator.applyInc();
            operator.applySetOnInsert();
            operator.applyPositional();
            operator.applyPush();
            operator.applyPop();
            operator.applyAddToSet();
            operator.applyEach();
            
            operator.applyElemMatch();
            while (t.isAlive()) {
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
