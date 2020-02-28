#!/usr/bin/env bash
GIT_REPO_URL=""
DOCKER_UNAME="saifnuaimii"
DOCKER_UPASS="S9494@@if"
DOCKER_IMAGE=$DOCKER_UNAME/app
DOCKER_IMAGE_VERSION="v1.0.0"

#git clone $GIT_REPO_URL APP
#cd APP

#----------------------------------------------------------

function docker_tag_exists() {
   TOKEN=$(curl -s -H "Content-Type: application/json" -X POST -d '{"username": "'${DOCKER_UNAME}'", "password": "'${DOCKER_UPASS}'"}' https://hub.docker.com/v2/users/login/ | jq -r .token)
   EXISTS=$(curl -s -H "Authorization: JWT ${TOKEN}" https://hub.docker.com/v2/repositories/$DOCKER_IMAGE/tags/\?page_size\=10000 | jq -r "[.results | .[] | .name == \"$DOCKER_IMAGE_VERSION\"] | any")
   test $EXISTS = true
}

if docker_tag_exists $DOCKER_IMAGE $DOCKER_IMAGE_VERSION; then
    echo $DOCKER_IMAGE $DOCKER_IMAGE_VERSION exist
else
    echo "missing/wrong version.Kindly check your docker repo or fix image name and tag"
    exit 1
fi
#----------------------------------------------------------
cd ..
docker build -t ${DOCKER_IMAGE}:${DOCKER_IMAGE_VERSION} -f APP/Dockerfile .

MY_DOCKER_RANDOM_NAME=myapp$RANDOM

docker run -d --rm --name $MY_DOCKER_RANDOM_NAME ${DOCKER_IMAGE}:${DOCKER_IMAGE_VERSION}
MY_DOCKER_ID=$(docker ps -aqf "name=$MY_DOCKER_RANDOM_NAME")
CONTAINER_STATUS=$(docker inspect -f {{.State.Running}} $MY_DOCKER_ID)

if [ $CONTAINER_STATUS = "true" ]; then
    echo "Your application working fine"
else
    echo "missing/wrong .Kindly check your docker continer"
    exit 1
fi
docker stop $MY_DOCKER_RANDOM_NAME
docker push $DOCKER_IMAGE