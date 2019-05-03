#!/usr/bin/env bash
WEB_DOCKER_IMAGE_ID=$(docker inspect registry.heroku.com/brain-heap/web --format={{.Id}})
echo "Image id = $WEB_DOCKER_IMAGE_ID"
curl -n -X PATCH https://api.heroku.com/apps/brain-heap/formation \
  -d '{
  "updates": [
  {
    "type": "web",
    "docker_image": "$WEB_DOCKER_IMAGE_ID"
  },
  {
    "type": "worker",
    "docker_image": "$WORKER_DOCKER_IMAGE_ID"
  },
  ]
}' \
-H "Content-Type: application/json" \
-H "Accept: application/vnd.heroku+json; version=3.docker-releases"