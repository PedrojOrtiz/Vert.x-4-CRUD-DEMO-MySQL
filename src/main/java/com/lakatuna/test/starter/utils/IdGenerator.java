package com.lakatuna.test.starter.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class IdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

    try {
      String generatedId = UUID.randomUUID().toString();
      return generatedId;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }
}
