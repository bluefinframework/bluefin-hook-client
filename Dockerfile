FROM maven:3.3.3

ADD pom.xml /home/build/
RUN cd /home/build && mvn -q dependency:resolve

ADD src /home/build/src

RUN cd /home/build && mvn -q -DskipTests=true package \
        && mv target/*.jar /app.jar \
        && cd / && rm -rf /home/build

 VOLUME /root
EXPOSE 2557
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
