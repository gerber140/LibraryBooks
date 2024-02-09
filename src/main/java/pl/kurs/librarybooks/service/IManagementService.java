package pl.kurs.librarybooks.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface IManagementService<T> {
    T add(T entity);
    T get(Long id);
    T edit(T entity);
    void delete(Long id);
    Page<T> getAll(int pageNumber, int pageSize, String value);

}
