package br.com.gx2.dao;


import java.util.List;
import java.util.Map;

import br.com.gx2.factory.dao.exception.DAOException;

/**
 *
 * @author Ronaldo Prass
 */
public interface GenericDAO<T, O> {

	public void create(T entidade) throws DAOException;

	public void update(T entity) throws DAOException;

	public void delete(T entity) throws DAOException;

	public List<T> listAll() throws DAOException;

	public T findById(Integer id) throws DAOException;

	public List<T> findByValueBetweenValue(String column, Object first, Object last) throws DAOException;

	public List<T> findByParams(Map<String, Object> params) throws DAOException;

	public List<T> findObjectByLimitAndOrderBy(String column, int first, int max) throws DAOException;

	public List<T> findObjectByLimitParams(Map<String, Object> params, int first, int max) throws DAOException;

	public List<T> findCriteriaQueryBetweenDate(String pattern, String column, String startDate, String endDate)
			throws DAOException;

	public List<T> findCriteriaQueryByDate(String pattern, String column, String startDate) throws DAOException;

	public List<T> findCriteriaQueryByDateAndOrderBy(String pattern, String column, String date, String orderColumn)
			throws DAOException;

	public List<T> findCriteriaParamsAndBetweenDate(Map<String, Object> params, String pattern, String column,
			String startDate, String endDate) throws DAOException;

	public List<T> findCriteriaParamsAndBetweenValue(Map<String, Object> params, String column, Object first,
			Object last) throws DAOException;

}