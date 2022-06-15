#bin/bash
#docker hub에 push 하기위해 login
docker login -u "jhleedev" -p "L2eJinH5!@"

#docker hub에 push
docker push docker.io/jhleedev/kafka-app:$(docker images | grep docker.io/jhleedev/kafka-app | awk {'print $2'})
