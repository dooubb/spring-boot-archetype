FROM  fanq1213/eclipse-temurin:17.0.6_10-jre-alpine
COPY ./target/${artifactId}-${version}.jar /usr/local/app.jar
ENV JVM_OPTION=""
EXPOSE 8080
ENTRYPOINT java ${JVM_OPTION} -XX:NativeMemoryTracking=summary -jar /usr/local/app.jar