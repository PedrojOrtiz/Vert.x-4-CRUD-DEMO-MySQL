package com.lakatuna.test.starter;

import com.lakatuna.test.starter.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    final Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(MainVerticle.class.getName())
      .onFailure(throwable -> System.exit(-1));

  }

}
