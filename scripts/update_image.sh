#!/bin/bash

VERSION=$1

sed -i "s|image:.*|image: naveen352/java-demo:$VERSION|g" deployment.yaml
