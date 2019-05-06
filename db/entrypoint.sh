#!/bin/bash

echo "Database initialization start"
(mongod --replSet 'brainheap-rs') &(
sleep 5;
mongo --eval "rs.initiate();"
echo "Replica Set status =>"
mongo --eval "rs.status();"
mongo --eval "rs.add(brainheap-db:27017');"
echo "Creating database =>"
mongorestore --drop --gzip --nsInclude 'brainheap.*' --archive=/brainheap_db_starter
echo "Database initialization end"
)