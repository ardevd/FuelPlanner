 curl -XDELETE 'http://es.olympus.com:9200/fuelplanner/'
 curl -XPUT 'http://es.olympus.com:9200/fuelplanner'
 curl -XPUT 'http://es.olympus.com:9200/fuelplanner/report/_mapping' -d '{
     "report" : {
         
         "_id": {
             "index" : "not_analyzed",
             "store" : true
         },
 
         "properties": {
 
         "added_time": {
             "type": "date"
         }      
 
     }
     }
 }'
