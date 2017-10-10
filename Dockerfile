FROM java:8
WORKDIR /
ADD build/libs/leaderboard.jar leaderboard.jar
EXPOSE 8080
CMD java -jar leaderboard.jar