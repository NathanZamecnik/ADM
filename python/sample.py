from pymongo import MongoClient
from pprint import pprint
import random
import string

def insert_bulk(col):
    """
    Given a collection instance we will generate 100 random documents and bulk insert.
    """
    #2 alternatives: ordered and unordered - http://docs.mongodb.org/manual/core/bulk-inserts/
    # unordered for this example
    ubulk = col.initialize_unordered_bulk_op()
    for i in range(100):
        ubulk.insert( {'msg': random.sample(string.letters, 20, "counter": i})
    #lets execute the bulk operation and collect back the result
    res = ubulk.execute()
    pprint("bulk.execute()")
    pprint(res)

def findAndModify(col):
    """
    Let's find the element that coints counter = 10 and increment it's value by 1
    """
    query = {'counter': 10}
    update = { "$inc": {"counter":1}}
    res = col.find_and_modify( query, update)
    pprint("find_and_modify")
    pprint(res)
    #if we collect it back we can see that we've updated it
    obj = col.find_one({"_id": res._id})
    pprint(obj)


def main():
    """
    Sample code to express the connectivity to MongoDB using the pymongo driver
    """
    mc = MongoClient("localhost:27017")
    db = mc.getDB("somedatabase")
    col = db['items']

    #bulk insert
    insert_bulk(col)

    #find_and_modify
    findAndModify(col)


if __name__ == "__main__":
    main()
