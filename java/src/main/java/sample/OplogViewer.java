package sample;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class OplogViewer implements Runnable {

    
    MongoClient mc;
    
    public OplogViewer(MongoClient mc){
        this.mc = mc;
    }
    
    
    public void run() {
        
        DBCollection oplog = this.mc.getDB("local").getCollection("oplog.rs");
        //{ $natural: 1 } 
        DBObject orderBy = new BasicDBObject("$natural", 1 );
        DBCursor cur = oplog.find().sort(orderBy);
        cur.setOptions(Bytes.QUERYOPTION_TAILABLE);
        
        if( cur.hasNext() ){
            
            for ( DBObject obj : cur ){
                System.out.println(obj);
            }
            System.out.println("hey");
        }
        

    }

}
