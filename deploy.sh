#bin/bash
#docker hub에 push 하기위해 login
# docker login -u "jhleedev" -p "L2eJinH5!@"

#docker image tag
docker image tag kafka-app:$(docker images | grep kafka-app | head -n 1 | awk {'print $2'}) 172.16.3.4:5000/jhleedev/kafka-app:$(docker images | grep kafka-app | head -n 1 | awk {'print $2'})
#docker image push
docker push kafka-app:$(docker images | grep kafka-app | head -n 1 | awk {'print $2'})
