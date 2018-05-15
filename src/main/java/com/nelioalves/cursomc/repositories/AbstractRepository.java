package com.nelioalves.cursomc.repositories;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.nelioalves.cursomc.entity.BaseEntity;

public abstract class AbstractRepository<E extends BaseEntity> {
	
	
	protected abstract Class<E> getClazz();
	
	private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public E save(E e) {
		entityManager.persist(e);
		return e;
	}

	public void remove(E e) {
		entityManager.remove(e);
	}

	public E update(E e) {
		return entityManager.merge(e);
	}
	
	public E find(Object id) {
		return entityManager.find(getClazz(), id);
	}

	public List<E> findAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(getClazz());
		Root<E> rootEntry = cq.from(getClazz());
		CriteriaQuery<E> all = cq.select(rootEntry);
		TypedQuery<E> allQuery = entityManager.createQuery(all);
		return allQuery.getResultList();
	}

}
