package com.lakatuna.test.starter.api.repository;

import com.lakatuna.test.starter.api.model.Book;
import org.hibernate.reactive.mutiny.Mutiny;

public class BookHibernateRepository extends AbstractHRepository<Book, String> {

  public BookHibernateRepository(Class<Book> clazz, Mutiny.SessionFactory sessionFactory) {
    super(clazz, sessionFactory);
  }

}
