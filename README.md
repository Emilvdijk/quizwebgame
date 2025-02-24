welcome to my quiz web game!

in this simple spring application a public api is consumed to grab some quiz questions which can
then be answered in my app! a user can also make an account to track progress and choose which kind
of questions they want to see.

I use these public api to get my questions:

https://the-trivia-api.com/

https://opentdb.com/

github:
https://github.com/Emilvdijk/quizwebgame

you can also pull it from dockerhub!

docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t emilvdijk/quizwebgame:test

author:
Emil van Dijk
