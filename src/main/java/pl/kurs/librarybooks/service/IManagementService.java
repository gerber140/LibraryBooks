package pl.kurs.librarybooks.service;

import java.util.List;

public interface IManagementService<T> {
    T add(T entity);
    T get(Long id);
    T edit(T entity);
    void delete(Long id);
    List<T> getAll();

}
