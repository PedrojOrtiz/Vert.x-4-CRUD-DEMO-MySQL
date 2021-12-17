package com.lakatuna.test.starter.api.service;

import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.model.BookGetAllResponse;
import com.lakatuna.test.starter.api.model.BookGetByIdResponse;
import com.lakatuna.test.starter.api.repository.BookHibernateRepository;
import com.lakatuna.test.starter.utils.QueryUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;
import java.util.stream.Collectors;


public class BookHibernateService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookHibernateService.class);

  private final BookHibernateRepository bookHibernateRepository;

  public BookHibernateService(BookHibernateRepository bookHibernateRepository) {
    this.bookHibernateRepository = bookHibernateRepository;
  }

//  public Uni<BookGetAllResponse> readAll(String p, String l) {
//
//    final int page = QueryUtils.getPage(p);
//    final int limit = QueryUtils.getLimit(l);
//    final int offset = QueryUtils.getOffset(page, limit);
//
//    return this.bookHibernateRepository.count(Book.class)
//      .flatMap(total ->
//        this.bookHibernateRepository.findAllByLimitOffset(limit, offset)
//          .map(result -> {
//            final List<BookGetByIdResponse> books = result.stream()
//              .map(BookGetByIdResponse::new).collect(Collectors.toList());
//              return  new BookGetAllResponse(total, limit, page, books);
//          })
//      );
//
//  }

//  public Uni<Book> findById(String id) {
//
//    return this.bookHibernateRepository.get(id);
//
//  }





  }
