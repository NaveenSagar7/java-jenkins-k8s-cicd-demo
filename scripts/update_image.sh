#!/bin/bash

VERSION=$1

sed -i "s|image:.*|image: dockerhub-user/java-demo:$VERSION|g" deployment.yaml
