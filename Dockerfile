FROM java:8
EXPOSE 8080
ADD myeasyblog-0.0.1-SNAPSHOT.jar blogapp.jar
RUN bash -c 'touch /blogapp.jar'
ENTRYPOINT ["java", "-jar", "/blogapp.jar", "--spring.profiles.active=pro"]