var MongoClient = require("mongodb").MongoClient

function insert_bulk(col){
    var bulk = col.initializeOrderedBulkOp();
    for( var i = 0; i < 100; i++){
    	bulk.insert( {msg: Math.random(), counter: i} );
    }

    var res = bulk.execute();
    console.log("bulk insert");
    console.log(res);
}


function findAndModify(col){

}

MongoClient.connect( "mongodb://nair.local:27017/node", function(err, db){ if(!err) { console.log("we are connected")}  })


var col = db.collection("sample");

insert_bulk(col)