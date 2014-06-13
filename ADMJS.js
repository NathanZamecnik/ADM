////////////////////////////////////////////////////
//Bulk Inserts
var bulk = function() {
var bulk = db.items.initializeUnorderedBulkOp();
print("var bulk = db.items.initializeUnorderedBulkOp();");
for(i=0;i<100;i++) { bulk.insert( { msg : Math.random(), counter : i } ); }
print("for(i=0;i<100;i++) { bulk.insert( { msg : Math.random(), counter : i } ); }")
var res = bulk.execute();
print("bulk.execute()")
print(res);
}
//for(i=0;i<100000;i++) { db.items.insert({ msg : Math.random(), counter : i } ); }
//var bulk = db.items.initializeUnorderedBulkOp();
//for(i=0;i<100000;i++) { bulk.insert( { msg : Math.random(), counter : i } ); }
//bulk.execute()

////////////////////////////////////////////////////
//findAndModify
var obj = db.items.findAndModify( { query : { counter : 10 }, update : { $inc : { counter : 1 } } } );
obj;
db.items.find( { _id : obj._id } );

//setup a time range and query for a doc that is locked but older
var timeRange = new ISODate();
timeRange.setMinutes( timeRange.getMinutes() - 1 );
timeRange;
db.fam.findAndModify( { query : { $or : [ { locked : false }, { locked : true, t : { $lt : timeRange } } ] }, update : { $set : { t : new ISODate(), locked : true } } } )

////////////////////////////////////////////////////
//collMod
db.runCommand( { collMod : 'myCollection', usePowerOf2Sizes : true } )




