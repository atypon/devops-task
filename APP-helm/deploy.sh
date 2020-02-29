#!/bin/bash

TILLER-NAMESPACE=""
NAMESPACE=""

helm upgrade \
    --install \
    --wait \
    --force \
    --timeout 900 \
    --tiller-namespace=$TILLER-NAMESPACE \
    --namespace=$NAMESPACE \
    my-app \
    .
