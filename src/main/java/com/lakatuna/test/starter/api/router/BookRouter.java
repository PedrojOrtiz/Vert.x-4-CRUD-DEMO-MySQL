package com.lakatuna.test.starter.api.router;

import com.lakatuna.test.starter.api.handler.BookHandler;
import com.lakatuna.test.starter.api.handler.BookValidationHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

public class BookRouter {

  private final Vertx vertx;
  private final BookHandler bookHandler;
  private final BookValidationHandler bookValidationHandler;

  public BookRouter(Vertx vertx, BookHandler bookHandler,
                    BookValidationHandler bookValidationHandler) {
    this.vertx = vertx;
    this.bookHandler = bookHandler;
    this.bookValidationHandler = bookValidationHandler;
  }


  /**
   * Set books API routes
   *
   * @param router Router
   */
  //8888
  public void setRouter(Router router) {
    router.mountSubRouter("/test/api", buildBookRouter());
  }

  /**
   * Build books API
   * All routes are composed by an error handler, a validation handler and the actual business logic handler
   */
  private Router buildBookRouter() {

    final Router bookRouter = Router.router(vertx);

    bookRouter.route("/books*").handler(BodyHandler.create());
    bookRouter.get("/books").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.readAll()).handler(bookHandler::readAll);
    bookRouter.get("/books/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.readOne()).handler(bookHandler::readOne);
    bookRouter.post("/books").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.create()).handler(bookHandler::create);
    bookRouter.put("/books/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.update()).handler(bookHandler::update);
    bookRouter.delete("/books/logicdelete/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.logicDelete()).handler(bookHandler::logicDelete);
    bookRouter.delete("/books/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookValidationHandler.delete()).handler(bookHandler::delete);

    return bookRouter;

  }

}
