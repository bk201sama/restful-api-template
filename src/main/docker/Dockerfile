FROM sgrio/java-oracle:jdk_8
ADD gaia.jar /usr/src/run_opt.jar
WORKDIR /usr/src
CMD ["java", "-Duser.timezone=Asia/Shanghai", "-Djava.security.egd=file:/dev/./urandom", "-jar", "run_opt.jar"]