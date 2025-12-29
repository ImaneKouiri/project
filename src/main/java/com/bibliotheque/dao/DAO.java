package com.bibliotheque.dao;

import java.util.List;

public interface DAO<T> {

  void create(T obj);

  T findById(int id);

  List<T> findAll();

  void update(T obj);

  void delete(int id);
}
