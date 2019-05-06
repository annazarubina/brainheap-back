# Brain heap

The goal of this project is to create an application, which will help people to extend their english vocabulary.

[![Build Status](https://travis-ci.org/annazarubina/brainheap-back.svg?branch=master)](https://travis-ci.org/annazarubina/brainheap-back)

**How run it locally** 

All commands must be executed in _{PATH_TO_PROJECT}/brainheap-back/_ directory.

**Package**

Run `./gradlew build`

To skip tests add `-x test` flag.

**Temporary solution for using mongoDB**

Docker-compose does not run the app and db correctly now, so for development
before run the app, please, run docker container with mongo image:

`docker run -p 27017 mongo`

**Start**

Run `docker-compose up --build -d`

The app listens on port `8080 `.

The database listens on port `27017`.
