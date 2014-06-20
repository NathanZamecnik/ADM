package main
import(
    "labix.org/v2/mgo"
    "labix.org/v2/mgo/bson"
    "fmt"
    "code.google.com/p/go-uuid/uuid"
)


func bulkInsert(col *mgo.Collection){
    msg := uuid.NewRandom().String()
    _len := 100
    bulk := make([]bson.M, _len)
    for i:= 0; i < _len; i++ {
        bulk[i] = bson.M{"msg":msg, "counter": i}
    }
    err := col.Insert(bulk)
    fmt.Print("Bulk Insert")
    fmt.Print(err)
}

func main(){
    session, err := mgo.Dial("localhost:27017")
    if err != nil {
        panic(err)
    }
    defer session.Close()
    session.SetSafe(&mgo.Safe{J: true})

    db := session.DB("go")
    col := db.C("sample")
    bulkInsert(col)

}
