# Reactive Fakebook (Facebook?!) part 1

Demo of Spring WebFlux using controller annotations.

This is similar to a Spring Web MVC web application, except that you can now use reactive types in your controller
to make your web application reactive.

This is part 1. Part 2 is the same application, but then using the Spring WebFlux functional web framework API.

## Running MongoDB in a Docker container

This webapp requires a MongoDB database.

A quick and easy way to get MongoDB running is by running it in a Docker container.
If you have Docker installed on your computer, you can start MongoDB with the following commands:

To pull the official MongoDB Docker image from the central Docker repository (you need to do this only once):

    docker pull mongo

To create and start a MongoDB Docker container:

    docker run --name mongo-demo -p 127.0.0.1:27017:27017 -d mongo

To see if the container is running:

    docker ps

To stop and delete the container:

    docker stop mongo-demo
    docker rm mongo-demo
