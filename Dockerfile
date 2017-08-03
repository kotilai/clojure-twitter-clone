FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/clojure-twitter-clone.jar /clojure-twitter-clone/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/clojure-twitter-clone/app.jar"]
