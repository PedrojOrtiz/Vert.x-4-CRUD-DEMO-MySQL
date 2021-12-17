package com.lakatuna.test.starter.verticle;

import com.lakatuna.test.starter.utils.LogUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  private static final String BOOKS_QUEUE = "bus.books.queue";

  @Override
  public void start() {

    System.out.println("Demo Project strats..");

    /**
     EventBus
     */
    EventBus eb = vertx.eventBus();

    /**
     * Consumer can return a MessageConsumer
     * without handler set, and then the handler.
     * This consumer will notify when a message is
     * received.
     */
    MessageConsumer<JsonObject> consumer = eb.consumer(BOOKS_QUEUE);
    consumer.handler(message -> {
      System.out.println("I have received a message: " + message.body());
    });

    /**
     * When registering a handler on a clustered event bus, it can take some
     * time for the registration to reach all nodes of the cluster.
     * If you want to be notified when this has completed, you can register a
     * completion handler the MessageConsumer object.
     */
//    consumer.completionHandler(res -> {
//      if (res.succeeded()) {
//        System.out.println("The handler registration has reached all nodes");
//      } else {
//        System.out.println("Registration failed!");
//      }
//    });

    /**
     * To unregister a handler, call
     * unregister. If you are on a clustered event bus,
     * un-registering can take some time to propagate
     * across the nodes. If you want to be notified
     * when this is complete, use unregister
     */
//    consumer.unregister(res -> {
//      if (res.succeeded()) {
//        System.out.println("The handler un-registration has reached all nodes");
//      } else {
//        System.out.println("Un-registration failed!");
//      }
//    });

    final long start = System.currentTimeMillis();
//    deployMigrationVerticle(vertx)
//      .flatMap(migrationVerticleId ->



    deployApiVerticle(vertx)
      .onSuccess(success -> LOGGER.info(LogUtils.RUN_APP_SUCCESSFULLY_MESSAGE.buildMessage(System.currentTimeMillis() - start)))
      .onFailure(throwable -> LOGGER.error(throwable.getMessage()));

    System.out.println("Starting Hibernate Verticle");

    final io.vertx.mutiny.core.Vertx MVertx = io.vertx.mutiny.core.Vertx.vertx();

    DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("myPort", 3306));

    MVertx.deployVerticle(HibernateVerticle::new, options).subscribe().with(  // <2>
      ok -> {
        LOGGER.info("✅ Deployment success");
      }, err -> LOGGER.error("🔥 Deployment failure", err));

  }

  private Future<Void> deployMigrationVerticle(Vertx vertx) {
    final DeploymentOptions options = new DeploymentOptions()
      .setWorker(true)
      .setWorkerPoolName("migrations-worker-pool")
      .setInstances(1)
      .setWorkerPoolSize(1);

    return vertx.deployVerticle(MigrationVerticle.class.getName(), options)
      .flatMap(vertx::undeploy);
  }

  private Future<String> deployApiVerticle(Vertx vertx) {
    return vertx.deployVerticle(ApiVerticle.class.getName());
  }

}
