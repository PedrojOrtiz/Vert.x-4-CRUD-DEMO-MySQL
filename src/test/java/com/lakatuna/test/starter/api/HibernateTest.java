package com.lakatuna.test.starter.api;

import com.lakatuna.test.starter.verticle.HibernateVerticle;
import com.lakatuna.test.starter.verticle.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClientRequest;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(VertxExtension.class)
public class HibernateTest {

  private static final Logger logger = LoggerFactory.getLogger(HibernateTest.class);

  Vertx vertx = Vertx.vertx();

  @BeforeAll
  public void setupAll(VertxTestContext testContext) {

    DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("myPort", 3307));

    vertx.deployVerticle(HibernateVerticle::new, options).subscribe().with(  // <2>
      ok -> {
        logger.info("✅ Deployment success " + ok);
        testContext.completeNow();
      },
      err -> {
        logger.info("🔥 Deployment failure");
        testContext.failNow(err);
      });
  }

  @Test
  public void testVertx(VertxTestContext testContext) {
    assertThat(vertx).isNotNull();
    testContext.completeNow();
  }

  @Test
  void testGetAll(VertxTestContext testContext) {
    logger.info("running test: testGetAll");
    var options = new HttpClientOptions()
      .setDefaultPort(8085);
    var client = vertx.createHttpClient(options);

    client.request(HttpMethod.GET, "/hr/api/books")
      .flatMap(HttpClientRequest::send)
      .flatMap(HttpClientResponse::body)
      .subscribe()
      .with(buffer ->
          testContext.verify(
            () -> {
              logger.info("response buffer: {0}" + new Object[]{buffer.toString()});
              assertThat(buffer.toJsonArray().size()).isGreaterThan(0);
              testContext.completeNow();
            }
          ),
        e -> {
          logger.info("error: {0} " + e.getMessage());
          testContext.failNow(e);
        }
      );
  }


}
