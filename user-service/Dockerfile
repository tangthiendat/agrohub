#Use this dockerfile if you not have Maven installed on your local machine
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY core/ core/
COPY user-service/ user-service/
COPY product-service/ product-service/
COPY inventory-service/ inventory-service/
COPY purchase-service/ purchase-service/
COPY debt-service/ debt-service/
COPY customer-service/ customer-service/
COPY sales-service/ sales-service/
COPY email-service/ email-service/

#Build only user-service and its dependent modules
RUN mvn clean package -DskipTests -pl user-service -am

FROM eclipse-temurin:21-jre-alpine

RUN unlink /etc/localtime;ln -s  /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime

WORKDIR /app

COPY --from=build /app/user-service/target/*.jar /app/user-service.jar

EXPOSE 8080

ENV JAVA_OPTIONS="-Xmx2048m -Xms256m"
ENV SPRING_PROFILES_ACTIVE="docker"

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTIONS} -jar /app/user-service.jar"]