#!/usr/bin/env bash
set e
#Variables
#----------------------------------------------------------
GIT_REPO_URL="https://github.com/saifal-nuaimi/devops-task.git"
DOCKER_UNAME="saifnuaimii"
DOCKER_UPASS="P@ssw0rdd"
DOCKER_IMAGE=$DOCKER_UNAME/app
DOCKER_IMAGE_VERSION="v1.0.0"

#----------------------------------------------------------
# cleaning
echo "cleaning ..."
rm -rf devops-task

#----------------------------------------------------------
echo "clone > $GIT_REPO_URL ..."
git clone $GIT_REPO_URL
#----------------------------------------------------------


# Check if tag exisit
function docker_tag_exists() {
  TOKEN=$(curl -s -H "Content-Type: application/json" -X POST -d '{"username": "'${DOCKER_UNAME}'", "password": "'${DOCKER_UPASS}'"}' https://hub.docker.com/v2/users/login/ | jq -r .token)
  EXISTS=$(curl -s -H "Authorization: JWT ${TOKEN}" https://hub.docker.com/v2/repositories/$DOCKER_IMAGE/tags/\?page_size\=10000 | jq -r "[.results | .[] | .name == \"$DOCKER_IMAGE_VERSION\"] | any")
  test $EXISTS = true
}

if docker_tag_exists $DOCKER_IMAGE $DOCKER_IMAGE_VERSION; then
  echo $DOCKER_IMAGE $DOCKER_IMAGE_VERSION exist
else
  echo "missing/wrong version.Kindly check your docker repo or fix image name and tag"
  exit 2
fi
#----------------------------------------------------------
# get right path.
cd ../..
cd task1/APP
#----------------------------------------------------------


# Building Docker Image
function build_docker_image() {
  docker build -t ${DOCKER_IMAGE}:${DOCKER_IMAGE_VERSION} -f Dockerfile .
}
if docker_tag_exists; then
   echo "Building your image"
   build_docker_image
else
  echo "missing/wrong .Building failed"
  exit 4
fi
#------------------------------------------------------
# run docker-compose to test the service
docker-compose up -d


# check if the docker container running
is_healthy() {
    service="webapp"
    container_id="$(docker-compose ps -q "$service")"
    health_status="$(docker inspect -f "{{.State.Status}}" "$container_id")"
    echo $health_status
    while [ "$health_status" = "running" ]
    do
      echo "container is running"
      sleep 10
      break
    done
    }

 is_healthy
#------------------------------------------------------------------------
# use the health actuator to check if the service working well

APP_STATUS=$(curl --silent http://localhost:8080/actuator/health | jq .status)
echo $APP_STATUS
if [ $APP_STATUS="UP" ]; then
  echo "Your application working fine"
else [$APP_STATUS="Down"]
  echo "missing/wrong .Application failed in testing"
  exit 5
fi
#-----------------------------------------------------
# Stop the docker-compose
docker-compose down
#-----------------------------------------------------
#
## Push the docker Image
docker_push_image() {
  docker push $DOCKER_IMAGE
}

if docker_push_image; then
  echo "Pushing your image"
  docker_push_image
else
  echo "missing/wrong .Pusing failed."
  exit 6
fi
##-----------------------------------------------------