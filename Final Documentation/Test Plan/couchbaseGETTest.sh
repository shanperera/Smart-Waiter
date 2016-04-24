#!/bin/bash
# Run script: ./couchbaseGETTest <iterations>
UserValue=$1

# loop forever
while true
do
  # start threads
  for i in $(seq 1 $UserValue)
  do
    echo "started instance no: $i"
    curl -X GET -H "Content-Type: application/text" "http://administrator:administrator@localhost:8091/pools/default/buckets/couchbaseevents/docs/789"
  done
done
