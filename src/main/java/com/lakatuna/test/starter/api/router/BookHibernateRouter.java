package com.lakatuna.test.starter.api.router;

import com.lakatuna.test.starter.api.handler.BookHandler;
import com.lakatuna.test.starter.api.handler.BookHibernateHandler;
import com.lakatuna.test.starter.api.handler.BookHibernateValidationHandler;
import com.lakatuna.test.starter.api.handler.BookValidationHandler;
//import io.vertx.core.Vertx;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
//import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.handler.BodyHandler;
import io.vertx.mutiny.ext.web.handler.LoggerHandler;

public class BookHibernateRouter {

  private final Vertx vertx;
  private final BookHibernateHandler bookHibernateHandler;
  //private final BookHibernateValidationHandler bookHibernateValidationHandler;

  public BookHibernateRouter(Vertx vertx, BookHibernateHandler bookHibernateHandler) {
    this.vertx = vertx;
    this.bookHibernateHandler = bookHibernateHandler;
    //this.bookHibernateValidationHandler = bookHibernateValidationHandler;
  }


  /**
   * Set books API routes
   *
   * @param router Router
   */
  public void setRouter(Router router) {
    router.mountSubRouter("/hr/api", buildBookRouter());
  }

  /**
   * Build books API
   * All routes are composed by an error handler, a validation handler and the actual business logic handler
   */
  private Router buildBookRouter() {

    final Router bookRouter = Router.router(vertx);

    bookRouter.route("/books*").handler(BodyHandler.create());

    bookRouter.get("/books").respond(bookHibernateHandler::list);
    bookRouter.get("/books/:id").respond(bookHibernateHandler::findById);
    bookRouter.post("/books").respond(bookHibernateHandler::create);
    bookRouter.delete("/books/:id").respond(bookHibernateHandler::delete);
    bookRouter.put("/books").respond(bookHibernateHandler::update);

    return bookRouter;

  }

}
