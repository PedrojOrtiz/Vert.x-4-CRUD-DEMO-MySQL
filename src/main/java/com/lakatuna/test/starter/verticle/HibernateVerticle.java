package com.lakatuna.test.starter.verticle;

import com.lakatuna.test.starter.api.handler.BookHibernateHandler;
import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.repository.BookHibernateRepository;
import com.lakatuna.test.starter.api.router.BookHibernateRouter;
import com.lakatuna.test.starter.api.service.BookHibernateService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.persistence.Persistence;
import java.util.Map;

public class HibernateVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateVerticle.class);
  private Mutiny.SessionFactory emf;

  private BookHibernateRepository bookHibernateRepository;
  private BookHibernateService bookHibernateService;
  private BookHibernateHandler bookHibernateHandler;
  private BookHibernateRouter bookHibernateRouter;

  @Override
  public Uni<Void> asyncStart() {
    System.out.println("Starting hibernate");

    final Router router = Router.router(vertx);

    Uni<Void> startHibernate = Uni.createFrom().deferred(() -> {

      var myPort = config().getInteger("myPort", 3306);
      var props = Map.of("javax.persistence.jdbc.url",
        "jdbc:mysql://localhost:" + myPort + "/books");

      emf = Persistence
        .createEntityManagerFactory("starter-persistence", props)
        .unwrap(Mutiny.SessionFactory.class);

      bookHibernateRepository = new BookHibernateRepository(Book.class, emf);
      bookHibernateService = new BookHibernateService(bookHibernateRepository);
      bookHibernateHandler = new BookHibernateHandler(bookHibernateService, bookHibernateRepository);
      bookHibernateRouter = new BookHibernateRouter(vertx, bookHibernateHandler);

      bookHibernateRouter.setRouter(router);


      return Uni.createFrom().voidItem();
    });

    startHibernate = vertx.executeBlocking(startHibernate)  // <2>
      .onItem().invoke(() -> LOGGER.info("✅ Hibernate Reactive is ready"));

    Uni<HttpServer> startHttpServer = vertx.createHttpServer()
      .requestHandler(router::handle)
      .listen(8085)
      .onItem().invoke(() -> LOGGER.info("✅ HTTP server listening on port 8085"));

    return Uni.combine().all().unis(startHibernate, startHttpServer).discardItems();  // <1>

  }
}
