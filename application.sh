#!/bin/bash

IMAGE_NAME="automaticbytes/demo-app"
CONTAINER_NAME="demo-app"
APP_URL="http://localhost:3100/health"

if [ "$1" == "start" ]; then
  if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Docker container $CONTAINER_NAME is already running."
  else
    docker run -d --name $CONTAINER_NAME
    -p 3100:3100 $IMAGE_NAME

    if [ $? -eq 0 ]; then
      echo "Docker container $CONTAINER_NAME started successfully."

      echo "Waiting for the application to become active..."
      while ! curl -s $APP_URL > /dev/null; do
        echo "Application is not active yet. Retrying in 5 seconds..."
        sleep 5
      done
      echo "Application is active!"
    else
      echo "Failed to start Docker container $CONTAINER_NAME."
      exit 1
    fi
  fi
elif [ "$1" == "stop" ]; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME

  if [ $? -eq 0 ]; then
    echo "Docker container $CONTAINER_NAME stopped and removed successfully."
  else
    echo "Failed to stop Docker container $CONTAINER_NAME."
    exit 1
  fi
else
  echo "Usage: $0 {start|stop}"
  exit 1
fi