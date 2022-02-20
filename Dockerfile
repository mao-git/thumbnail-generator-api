FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/thumbnail-generator-api-0.0.1.jar thumbnail.jar
ENTRYPOINT ["java", "-jar", "/thumbnail.jar"]