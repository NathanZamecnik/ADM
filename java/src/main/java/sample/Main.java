package sample;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Main {


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
            
            
            BulkInsert bulkInsert = new BulkInsert(db.getCollection("bulkdata"));
            bulkInsert.orderedBulkInsert();
            bulkInsert.unOrderedBulkInsert();
            
            
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
