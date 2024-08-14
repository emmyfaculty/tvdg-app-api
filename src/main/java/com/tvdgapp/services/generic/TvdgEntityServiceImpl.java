package com.tvdgapp.services.generic;


import com.tvdgapp.exceptions.EntityType;
import com.tvdgapp.exceptions.ExceptionType;
import com.tvdgapp.exceptions.ServiceException;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * @param <T> entity type
 */
@RequiredArgsConstructor
@SuppressWarnings("NullAway.Init")
public abstract class TvdgEntityServiceImpl<K extends Serializable & Comparable<K>, E extends TvdgAppEntity<K, ?>>
        implements TvdgEntityService<K, E> {

    /**
     * Class of the entity, determined from the generic parameters.
     */
    private  Class<E> objectClass;

    private  JpaRepository<E, K> repository;

    @SuppressWarnings("unchecked")
    public TvdgEntityServiceImpl(JpaRepository<E, K> repository) {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.objectClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
        this.repository = repository;
    }

    @Nullable
    protected final Class<E> getObjectClass() {
        return objectClass;
    }


    public E getById(K id) {
        return repository.getOne(id);
    }


    public void save(E entity) throws ServiceException {
        repository.saveAndFlush(entity);
    }

    public Optional<E> findById(K id) {
        return repository.findById(id);
    }


    public void create(E entity) throws ServiceException {
        save(entity);
    }


    public void update(E entity) throws ServiceException {
        save(entity);
    }


    public void delete(E entity) throws ServiceException {
        repository.delete(entity);
    }


    public void flush() {
        repository.flush();
    }


    public List<E> list() {
        return repository.findAll();
    }


    public Long count() {
        return repository.count();
    }

    protected E saveAndFlush(E entity) {
        return repository.saveAndFlush(entity);
    }


    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    public RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TvdgException.throwException(entityType, exceptionType, args);
    }

    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    public RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, Integer id, String... args) {
        return TvdgException.throwExceptionWithId(entityType, exceptionType, id, args);
    }

}