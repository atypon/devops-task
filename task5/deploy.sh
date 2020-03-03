#!/bin/bash
cd helm
helm upgrade \
    --install \
    --wait \
    --force \
    --timeout 900 \
    --tiller-namespace=""\
    --namespace="" \
    my-app \
    .
