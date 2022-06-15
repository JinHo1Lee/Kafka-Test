#!/bin/sh

java -jar -Dspring.config.location=/config/ -Dspring.profiles.active=${PROFILE} /app.jar