package com.lakatuna.test.starter.api.handler;

import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.model.BookGetAllResponse;
import com.lakatuna.test.starter.api.model.BookGetByIdResponse;
import com.lakatuna.test.starter.api.service.BookService;
import com.lakatuna.test.starter.utils.ResponseUtils;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public class BookHandler {

  private static final String ID_PARAMETER = "id";
  private static final String PAGE_PARAMETER = "page";
  private static final String LIMIT_PARAMETER = "limit";

  private final BookService bookService;

  public BookHandler(BookService bookService) {
    this.bookService = bookService;
  }

  /**
   * Read all books
   * It should return 200 OK in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetAllResponse
   */
  public Future<BookGetAllResponse> readAll(RoutingContext rc) {
    final String page = rc.queryParams().get(PAGE_PARAMETER);
    final String limit = rc.queryParams().get(LIMIT_PARAMETER);

    return bookService.readAll(page, limit)
      .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

  /**
   * Read one book
   * It should return 200 OK in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> readOne(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);

    return bookService.readOne(id)
      .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

  /**
   * Create one book
   * It should return 201 Created in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> create(RoutingContext rc) {
    final Book book = rc.getBodyAsJson().mapTo(Book.class);

    return bookService.create(book)
      .onSuccess(success -> ResponseUtils.buildCreatedResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

  /**
   * Update one book
   * It should return 200 OK in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> update(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);
    final Book book = rc.getBodyAsJson().mapTo(Book.class);

    return bookService.update(id, book)
      .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

  /**
   * Update one book
   * It should return 200 OK in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> logicDelete(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);

    final Book book = new Book();

    return bookService.readOne(id).map(res -> {
      System.out.println("Lee y asigna un libro");
      book.setId(res.getId());
      book.setAuthor(res.getAuthor());
      book.setCountry(res.getCountry());
      book.setImageLink(res.getImageLink());
      book.setLanguage(res.getLanguage());
      book.setLink(res.getLink());
      book.setPages(res.getPages());
      book.setTitle(res.getTitle());
      book.setYear(res.getYear());
      book.setActive(false);
      return res;
    }).onSuccess( s -> {
      bookService.update(id, book)
        .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
        .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
      }).onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));

  }

  /**
   * Delete one book
   * It should return 204 No Content in case of success
   * It should return 400 Bad Request, 404 Not Found or 500 Internal Server Error in case of failure
   *
   * @param rc Routing context
   * @return BookGetByIdResponse
   */
  public Future<Void> delete(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);

    return bookService.delete(id)
      .onSuccess(success -> ResponseUtils.buildNoContentResponse(rc))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

}
