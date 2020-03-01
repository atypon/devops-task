#!/bin/bash
cd APP-helm
helm upgrade \
    --install \
    --wait \
    --force \
    --timeout 900 \
    --tiller-namespace=""\
    --namespace="" \
    my-app \
    .
