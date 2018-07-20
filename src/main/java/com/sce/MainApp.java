package com.sce;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainApp  extends AbstractVerticle {

    public static void main(String... args) {
        Vertx.vertx().createHttpServer().requestHandler(req -> req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!")).listen(8080);
    }

}

