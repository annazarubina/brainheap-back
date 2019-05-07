#!/bin/bash

set -m

echo "Database initialization start"
echo "MONGO_SERVER=${MONGO_SERVER}, MONGO_PORT=${MONGO_PORT}, MONGO_DB=${MONGO_DB}"
(mongod --replSet 'brainheap-rs') &(
sleep 5;
mongo --eval "rs.initiate();"
echo "Replica Set status =>"
mongo --eval "rs.status();"
mongo --eval "rs.add(${MONGO_SERVER}:27017');"
echo "Creating database =>"
mongorestore --drop --gzip --nsInclude '${MONGO_DB}.*' --archive=/${MONGO_DB_STARTER}
echo "Database initialization end"
)
fg