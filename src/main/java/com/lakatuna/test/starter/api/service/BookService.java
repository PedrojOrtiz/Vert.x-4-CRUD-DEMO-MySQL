package com.lakatuna.test.starter.api.service;

import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.model.BookGetAllResponse;
import com.lakatuna.test.starter.api.model.BookGetByIdResponse;
import com.lakatuna.test.starter.api.repository.BookRepository;
import com.lakatuna.test.starter.utils.LogUtils;
import com.lakatuna.test.starter.utils.QueryUtils;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
  private static final String BOOKS_QUEUE = "bus.books.queue";

  private final MySQLPool dbClient;
  private final BookRepository bookRepository;
  private final Vertx vertx;

  public BookService(MySQLPool dbClient, BookRepository bookRepository, Vertx vertx) {
    this.dbClient = dbClient;
    this.bookRepository = bookRepository;
    this.vertx = vertx;
  }

  public Future<BookGetAllResponse> readAll(String p, String l) {

    return dbClient.withTransaction(
        connection -> {

          final int page = QueryUtils.getPage(p);
          final int limit = QueryUtils.getLimit(l);
          final int offset = QueryUtils.getOffset(page, limit);

          return bookRepository.count(connection)
            .flatMap(total ->
              bookRepository.selectAll(connection, limit, offset)
                .map(result -> {
                  final List<BookGetByIdResponse> books = result.stream()
                    .map(BookGetByIdResponse::new)
                    .map(b -> {
                      this.sendMessageToBooksQueue(b.getId());
                      return b;
                    })
                    .collect(Collectors.toList());

                  return new BookGetAllResponse(total, limit, page, books);

                })
            );

        })
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read all books", success.getBooks())))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read all books", throwable.getMessage())));

  }

  public void sendMessageToBooksQueue(String bookId) {

    if (!bookId.isBlank() || !bookId.isEmpty()) {
      LOGGER.info("send message to " + BOOKS_QUEUE + " with book id: " + bookId);
      this.vertx.eventBus().publish(BOOKS_QUEUE, new JsonObject().put("book_id", bookId));

    }

  }

  /**
   * Read one book
   *
   * @param id Book ID
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> readOne(String id) {
    return dbClient.withTransaction(
        connection -> bookRepository.selectById(connection, id)
          .map(BookGetByIdResponse::new))
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read one book", success)))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read one book", throwable.getMessage())));
  }

  /**
   * Create one book
   *
   * @param book Book
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> create(Book book) {
    book.setId(UUID.randomUUID().toString());
    book.setActive(true);
    return dbClient.withTransaction(
        connection -> bookRepository.insert(connection, book)
          .map(BookGetByIdResponse::new))
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Create one book", success)))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Create one book", throwable.getMessage())));
  }

  /**
   * Update one book
   *
   * @param id   Book ID
   * @param book Book
   * @return BookGetByIdResponse
   */
  public Future<BookGetByIdResponse> update(String id, Book book) {

    book.setId(id);

    return dbClient.withTransaction(
        connection -> bookRepository.update(connection, book)
          .map(BookGetByIdResponse::new))
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Update one book", success)))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Update one book", throwable.getMessage())));
  }

  /**
   * Logic delete one book
   * no object response, just message
   *
   * @param id Book ID
   * @return BookGetByIdResponse
   */
  public Future<Void> logicDelete(String id) {
    return dbClient.withTransaction(
        connection -> bookRepository.logicDelete(connection, id))
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("LogicDelete one book", id)))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("LogicDelete one book", throwable.getMessage())));
  }

  /**
   * Delete one book
   *
   * @param id Book ID
   * @return Void
   */
  public Future<Void> delete(String id) {
    return dbClient.withTransaction(
        connection -> bookRepository.delete(connection, id))
      .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Delete one book", id)))
      .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Delete one book", throwable.getMessage())));
  }

}
