# Gotsoccer Compare

The Gotsoccer Compare app takes two schedule spreadsheets and shows the differences. The user can upload 
a spreadsheet of games of a prior date, and one with a later date. Upon submission the app will calculate the differences.
There are two sections: one to show the game changes and the other to show the new games.

## Local Embedded Tomcat

```shell
- ./gradlew test
- ./gradlew bootRun
- ./gradlew bootWar
- java -jar .\build\libs\gotsoccer-compare-1.0.0.war
```

## Local Docker Deploy

```shell
- docker image build -t app .
- docker images
- docker run -d -p 8080:8080 -it app 
```

## Fly.io Deploy

```shell
- fly auth login
- google id as login
- fly launch (for initial deploy)

- ./gradlew build
- fly deploy
- fly logs -a gotsoccer-compare-young-glade-7918

https://gotsoccer-compare-young-glade-7918.fly.dev/index

```