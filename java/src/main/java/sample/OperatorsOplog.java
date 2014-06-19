package sample;

import java.util.UUID;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.QueryOperators;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class OperatorsOplog {

    
    private DBCollection collection;
    
    public OperatorsOplog( DBCollection collection){
        this.collection = collection;
    }
    
    
    /**
     * Shows the effect that a $set operation has over the oplog.     
     */
    public void applySet(){
        //lets generate a random UUID
        String _id = UUID.randomUUID().toString();
        DBObject obj = new BasicDBObject( "_id", _id );
        obj.put("someotherfield", "someothervalue");
        //let's insert the value
        this.collection.insert(obj);
        
        //let's $set a new field 
        System.out.println("Applying $set");
        DBObject query = new BasicDBObject("_id", _id);
        DBObject update = new BasicDBObject( "$set", new BasicDBObject("name", "Norberto") );
        this.collection.update(query, update);
    }
    
    public void applyInc() throws InterruptedException{
        //lets insert new document
        String _id = "inc";
        DBObject obj = new BasicDBObject("_id", _id);
        this.collection.insert(obj, WriteConcern.JOURNAL_SAFE);
        
        System.out.println("Applying $inc");
        //let's apply $inc over a non-existing field
        DBObject query = new BasicDBObject("_id", _id);
        //{$inc:{a:5}}
        DBObject update = new BasicDBObject("$inc", new BasicDBObject("a", 5));
        this.collection.update(query, update);
        Thread.sleep(100);
        //let's do it again but now on a existing field
        System.out.println("Applying $inc on existing fields");
        update = new BasicDBObject("$inc", new BasicDBObject("a", 1));
        this.collection.update(query, update);
    }
    
 
    public void applySetOnInsert() throws InterruptedException{
        String _id = "upsert";
        DBObject obj = new BasicDBObject("_id", _id);
        
        //{$setOnInsert: { name: "Norberto" }}
        System.out.println("Applying $setOnIsert");
        DBObject update = new BasicDBObject( "$setOnInsert", new BasicDBObject("name", "Norberto") );
        this.collection.update(obj, update, true, false);
        Thread.sleep(100);
        //again but this time the object exists
        System.out.println("Applying $setOnIsert when document exists");
        update = new BasicDBObject( "$setOnInsert", new BasicDBObject("name", "Nathan") );
        WriteResult res = this.collection.update(obj, update, true, false);
        System.out.println(res);
    }
    
    
    public void applyPositional(){
        //let's insert a document that contains an array field
        String _id = "positonal";
        DBObject obj = new BasicDBObject("_id", _id);
        int[] arr = {1,2,3};
        obj.put("arr", arr);
        this.collection.insert(obj, WriteConcern.FSYNC_SAFE);
        //increment the value 2 by 10 = 12
        
        
        System.out.println("Applying update $ positional on array");
        //{arr: 2}
        DBObject query = new BasicDBObject("arr", 2);
        //{$inc:{arr.$: 10}}
        DBObject update = new BasicDBObject( "$inc", new BasicDBObject( "arr.$", 10 )  );
        this.collection.update(query, update);
    }
    
    
    public void applyPush() throws InterruptedException{
        //let's insert document
        String _id = "push";
        DBObject obj = new BasicDBObject("_id", _id);
        this.collection.insert(obj, WriteConcern.FSYNC_SAFE);
        
        //let's push a value to new field
        System.out.println("Applying push to new field");
        DBObject query = new BasicDBObject("_id", _id);
        //{$push:{arr: 1}}
        DBObject update = new BasicDBObject( "$push", new BasicDBObject( "arr", 1 )  );
        this.collection.update(query, update);
        Thread.sleep(100);
        System.out.println("Applying push to existing field");
        update = new BasicDBObject( "$push", new BasicDBObject( "arr", 20 )  );
        this.collection.update(query, update);
    }

    
    public void applyPop() throws InterruptedException{
        //let's insert document
        String _id = "pop";
        DBObject obj = new BasicDBObject("_id", _id);
        int[] arr = {1,2,3,4,5};
        obj.put("arr", arr);
        this.collection.insert(obj, WriteConcern.FSYNC_SAFE);
        
        System.out.println("Applying pop");
        DBObject query = new BasicDBObject("_id", _id);
        //{$pop: {arr: 1 }}
        DBObject update = new BasicDBObject( "$pop" , new BasicDBObject( "arr", 1 )  );
        this.collection.update(query, update);
        Thread.sleep(100);
        System.out.println("Applying inverse pop");
        //{$pop: {arr: -1 }}
        update = new BasicDBObject( "$pop" , new BasicDBObject( "arr", -1 )  );
        this.collection.update(query, update);
        
//        Thread.sleep(100);
//        System.out.println("Applying pull");
//        //{$pull: {arr: 1 }}
//        update = new BasicDBObject( "$pull" , new BasicDBObject( "arr", 1 )  );
//        this.collection.update(query, update);
        //TODO Check the $pull statement 
    }
    
    
    public void applyAddToSet() throws InterruptedException{
        String _id = "addToSet";
        DBObject obj = new BasicDBObject("_id", _id);
        int[] arr = {1,2,3,4,5};
        obj.put("arr", arr);
        this.collection.insert(obj, WriteConcern.NORMAL);
        
        
        System.out.println("Applying addToSet");
        DBObject query = new BasicDBObject("_id", _id);
        //{$addToSet: {arr: 10 }}
        DBObject update = new BasicDBObject( "$addToSet" , new BasicDBObject( "arr", 10 )  );
        this.collection.update(query, update);
        
        System.out.println("Applying addToSet existing element");
//        Thread.sleep(100);
        query = new BasicDBObject("_id", _id);
        update = new BasicDBObject( "$addToSet" , new BasicDBObject( "arr", 5 )  );
        this.collection.update(query, update);
    }
    
    
    public void applyEach(){
        
    }
    
    
    public void applySort(){
        
    }
    
    
    public void applySlice(){
        
    }
    
    
    public void applyElemMatch() {
        //generate document with array of documents
        String _id = "elemMatch";
        DBObject obj = new BasicDBObject("_id", _id);
        BasicDBList list = new BasicDBList();
        for( int i = 0; i < 10; i++){
            DBObject o = new BasicDBObject("position", i);
            o.put("name", "norberto");
            list.add(o);
        }
        obj.put("arr", list);
        
        this.collection.insert(obj);
        
        
        //{ arr: $elemMatch:{ position: {$gt: 5}  }}
        DBObject match = new BasicDBObject("position", new BasicDBObject("$gt", 5) );
        DBObject query = QueryBuilder.start("arr").elemMatch(match).get();
        System.out.println("Applying $elemMatch");
        for ( DBObject o: this.collection.find(query)){
            System.out.println(o);
        }
        
//        DBObject update = new BasicDBObject( "$inc", new BasicDBObject("arr.position", 10)  );
//        WriteResult res = this.collection.update(query, update);
//        System.out.println(res);
    }
    
    
}
