#mvn package -f pom.xml
docker build -f "./Dockerfile" . -t "api-test"
docker tag api-test api-test:1
cd docker-compose
docker-compose up -d
