package com.lakatuna.test.starter.api.repository;

import com.lakatuna.test.starter.api.model.Book;
import org.hibernate.reactive.mutiny.Mutiny;

public class BookHibernateRepository extends AbstractHibernateRepository<Book, String> {

  public BookHibernateRepository(Class<Book> clazz, Mutiny.SessionFactory sessionFactory) {
    super(clazz, sessionFactory);
  }

}
