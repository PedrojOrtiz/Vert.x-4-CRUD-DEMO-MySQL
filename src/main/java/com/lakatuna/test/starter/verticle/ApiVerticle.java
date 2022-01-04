package com.lakatuna.test.starter.verticle;

import com.lakatuna.test.starter.api.handler.BookHandler;
import com.lakatuna.test.starter.api.handler.BookValidationHandler;
import com.lakatuna.test.starter.api.handler.ErrorHandler;
import com.lakatuna.test.starter.api.repository.BookRepository;
import com.lakatuna.test.starter.api.router.BookRouter;
import com.lakatuna.test.starter.api.router.HealthCheckRouter;
import com.lakatuna.test.starter.api.router.MetricsRouter;
import com.lakatuna.test.starter.api.service.BookService;
import com.lakatuna.test.starter.utils.DbUtils;
import com.lakatuna.test.starter.utils.LogUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLPool;

public class ApiVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiVerticle.class);

  @Override
  public void start(Promise<Void> promise) {

    //Pool Connexion
    final MySQLPool dbClient = DbUtils.buildDbClient(vertx);


    //DB Pool
    final BookRepository bookRepository = new BookRepository();
    final BookService bookService = new BookService(dbClient, bookRepository, vertx);
    final BookHandler bookHandler = new BookHandler(bookService);
    final BookValidationHandler bookValidationHandler = new BookValidationHandler(vertx);
    final BookRouter bookRouter = new BookRouter(vertx, bookHandler, bookValidationHandler);



    final Router router = Router.router(vertx);


    ErrorHandler.buildHandler(router);
    HealthCheckRouter.setRouter(vertx, router, dbClient);
    MetricsRouter.setRouter(router);

    bookRouter.setRouter(router);

    buildHttpServer(vertx, promise, router);

  }

  /**
   * Run HTTP server on port 8888 with specified routes
   *
   * @param vertx   Vertx context
   * @param promise Callback
   * @param router  Router
   */
  private void buildHttpServer(Vertx vertx, Promise<Void> promise, Router router) {

    final int port = 8888;

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          promise.complete();
          LOGGER.info(LogUtils.RUN_HTTP_SERVER_SUCCESS_MESSAGE.buildMessage(port));
        } else {
          promise.fail(http.cause());
          LOGGER.info(LogUtils.RUN_HTTP_SERVER_ERROR_MESSAGE.buildMessage());
        }
      });
  }

}
