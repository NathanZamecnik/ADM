package sample;

import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class BulkInsert {

    private DBCollection collection;

    public BulkInsert(DBCollection collection) {
        super();
        this.collection = collection;
    }
    
    /**
     * Inserts 100 documents over a bulk operation.
     */
    public void unOrderedBulkInsert(){
        this.unOrderedBulkInsert(100);
    }
    
    public void unOrderedBulkInsert(final int n){
        BulkWriteOperation bulk = this.collection.initializeUnorderedBulkOperation();
        for( int i = 0; i< n; i++){
            DBObject o = new BasicDBObject();
            o.put("counter", i);
            o.put("msg", UUID.randomUUID().toString());
            bulk.insert(o);
        }
        System.out.println("Bulk Insert (unordered)");
        BulkWriteResult res = bulk.execute();
        System.out.println(res);
    }
    
    
    public void orderedBulkInsert(){
        BulkWriteOperation bulk = this.collection.initializeOrderedBulkOperation();
        
        DBObject insert1 = new BasicDBObject("_id", "one");
        bulk.insert(insert1);
        DBObject insert2 = new BasicDBObject("_id", "two");
        bulk.insert(insert2);
        //update non-existing (not upsert) that should fail
        DBObject update3 = new BasicDBObject("$set", new BasicDBObject("msg",  UUID.randomUUID().toString()));
        bulk.find(new BasicDBObject("_id", "three")).update(update3);
        DBObject insert3 = new BasicDBObject("_id", "tree");
        bulk.insert(insert3);
        
        System.out.println("Bulk Insert ordered");
        BulkWriteResult res = bulk.execute();
        System.out.println(res);
    }
    
}
