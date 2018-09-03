FROM sgrio/java-oracle:jdk_8
ADD gaia.jar /usr/src/run_opt.jar
WORKDIR /usr/src
CMD ["java", "-server","-Xmx256m","-Xms256m","-Xmn100m","-Xss256k","-XX:+DisableExplicitGC","-XX:+UseConcMarkSweepGC","-XX:+CMSParallelRemarkEnabled","-XX:+UseFastAccessorMethods","-XX:+UseCMSInitiatingOccupancyOnly","-XX:CMSInitiatingOccupancyFraction=70","-Duser.timezone=Asia/Shanghai", "-Djava.security.egd=file:/dev/./urandom", "-jar", "run_opt.jar"]