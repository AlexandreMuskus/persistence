package br.com.gx2.dao.imp;

import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.gx2.dao.GenericDAO;
import br.com.gx2.factory.HibernateUtil;
import br.com.gx2.factory.dao.exception.DAOException;

/**
 *
 * @author Ronaldo Prass
 */
public abstract class HibernateGenericDAO<T, O> implements GenericDAO<T, O> {

	private Class<T> persistentClass;
	protected Session session;
	protected Logger logger = Logger.getLogger(HibernateGenericDAO.class);

	public HibernateGenericDAO() {
		// this.session = session;
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Session getSession() {

		return this.session;
	}

	public void create(T entidade) throws DAOException {
		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		Transaction tx = null;
		try {
			logger.info("inicializa a transação");
			tx = session.beginTransaction();
			logger.info("salva o registro");
			session.save(entidade);
			tx.commit();

		} catch (HibernateException e) {
			logger.info("desfaz a transação");
			tx.rollback();
			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("encerra a sessão");
			session.close();
		}

	}

	public void delete(T entity) throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		Transaction tx = null;
		try {
			logger.info("Inicializa a transação");
			tx = session.beginTransaction();
			logger.info("Remove o registro");
			session.delete(entity);
			logger.info("Commita a transação");
			tx.commit();

		} catch (HibernateException e) {
			tx.rollback();
			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}
	}

	public List<T> listAll() throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		try {
			return this.session.createCriteria(this.persistentClass).list();

		} catch (HibernateException e) {

			logger.error("erro ao tentar realizar a operação");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}

	}

	public void update(T entity) throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		Transaction tx = null;
		try {
			logger.info("Inicializa a transação");
			tx = session.beginTransaction();
			logger.info("Altera o registro");
			session.update(entity);
			tx.commit();

		} catch (HibernateException e) {
			tx.rollback();
			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}
	}

	public T findById(Integer id) throws DAOException {

		try {
			logger.info("conecta no banco de dados");
			session = HibernateUtil.getInstance();
			return (T) this.session.get(this.persistentClass, id);
		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();

		}

	}

	public List<T> findByValueBetweenValue(String column, Object first, Object last) throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		try {
			logger.info("Retorna o resultado de consulta");
			Criteria criteria = session.createCriteria(this.persistentClass);

			criteria.add(Restrictions.between(column, first, last));

			return criteria.list();

		} catch (Exception e) {

			logger.error("erro ao tentar recuperar os registros");
			throw new DAOException("Erro ao tentar recuperar os registros: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}

	}

	public List<T> findByParams(Map<String, Object> params) throws DAOException {

		try {

			logger.info("conecta no banco de dados");
			session = HibernateUtil.getInstance();

			logger.info(
					"Define o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria criteria = session.createCriteria(this.persistentClass);
			Set s = params.entrySet();

			Iterator it = s.iterator();

			logger.info("Constrói a lista de parâmetros usadas na consulta ");
			while (it.hasNext()) {

				Map.Entry m = (Map.Entry) it.next();
				String key = (String) m.getKey();
				Object value = m.getValue();
				logger.info("Adiciona as restrições na consulta HQL " + "coluna: " + m.getKey() + " - valor: "
						+ m.getValue());
				criteria.add(Restrictions.like(key, value));
			}

			logger.info("Retorna uma lista com os registros encontrados na consulta");
			List list = criteria.list();

			return list;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();

		}

	}

	public List<T> findObjectByLimitAndOrderBy(String column, int first, int max) throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();

		try {
			logger.info("Retorna uma lista com os registros encontrados na consulta");
			return session.createCriteria(this.persistentClass).setMaxResults(max).setFirstResult(first)
					.addOrder(Order.desc(column)).list();
		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}
	}

	public List<T> findObjectByLimitParams(Map<String, Object> params, int first, int max) throws DAOException {

		try {

			logger.info("conecta no banco de dados");
			session = HibernateUtil.getInstance();

			logger.info(
					"Definie o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria criteria = session.createCriteria(this.persistentClass);
			Set s = params.entrySet();

			Iterator it = s.iterator();

			logger.info("Constrói a lista de parâmetros usadas na consulta ");
			while (it.hasNext()) {

				Map.Entry m = (Map.Entry) it.next();
				String key = (String) m.getKey();
				Object value = m.getValue();
				logger.info("Adiciona as restrições na consulta HQL " + "coluna: " + m.getKey() + " - valor: "
						+ m.getValue());
				criteria.add(Restrictions.like(key, value));
			}
			criteria.setMaxResults(max);
			criteria.setFirstResult(first);
			logger.info("Retorna uma lista com os registros encontrados na consulta");
			List list = criteria.list();

			return list;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();

		}
	}

	public List<T> findCriteriaQueryBetweenDate(String pattern, String column, String startDate, String endDate)
			throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		try {

			logger.info(
					"Definie o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria crit = session.createCriteria(this.persistentClass);
			DateFormat format = new SimpleDateFormat(pattern);
			Date start = (Date) format.parse(startDate);
			Date end = (Date) format.parse(endDate);
			crit.add(Expression.between(column, new Date(start.getTime()), new Date(end.getTime())));
			logger.info("Retorna uma lista com os registros encontrados na consulta");
			List<T> records = crit.list();

			return records;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();
		}

	}

	public List<T> findCriteriaQueryByDate(String pattern, String column, String date) throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		try {
			logger.info(
					"Definie o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria crit = session.createCriteria(this.persistentClass);
			DateFormat format = new SimpleDateFormat(pattern);
			Date dateParam = (Date) format.parse(date);
			crit.add(Expression.eq(column, new Date(dateParam.getTime())));
			logger.info("Retorna uma lista com os registros encontrados na consulta");
			List<T> records = crit.list();

			return records;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}

	}

	public List<T> findCriteriaQueryByDateAndOrderBy(String pattern, String column, String date, String orderColumn)
			throws DAOException {

		logger.info("conecta no banco de dados");
		session = HibernateUtil.getInstance();
		try {
			logger.info(
					"Definie o POJO correspondente a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria crit = session.createCriteria(this.persistentClass);
			logger.info("Definie a coluna de ordenação: " + orderColumn);
			crit.addOrder(Order.desc(orderColumn));
			DateFormat format = new SimpleDateFormat(pattern);
			Date dateParam = (Date) format.parse(date);
			crit.add(Expression.eq(column, new Date(dateParam.getTime())));
			logger.info("Retorna uma lista com os registros encontrados na consulta");
			List<T> records = crit.list();

			return records;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {
			logger.info("Encerra a conexão");
			session.close();
		}

	}

	public List<T> findCriteriaParamsAndBetweenDate(Map<String, Object> params, String pattern, String column,
			String startDate, String endDate) throws DAOException {

		try {

			logger.info("conecta no banco de dados");
			session = HibernateUtil.getInstance();

			logger.info(
					"Definie o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria criteria = session.createCriteria(this.persistentClass);
			DateFormat format = new SimpleDateFormat(pattern);
			Date start = (Date) format.parse(startDate);
			Date end = (Date) format.parse(endDate);
			Set s = params.entrySet();

			Iterator it = s.iterator();

			logger.info("Constrói a lista de parâmetros usadas na consulta ");
			while (it.hasNext()) {

				Map.Entry m = (Map.Entry) it.next();
				String key = (String) m.getKey();
				Object value = m.getValue();
				logger.info("Adiciona as restrições na consulta HQL " + "coluna: " + m.getKey() + " - valor: "
						+ m.getValue());
				criteria.add(Restrictions.like(key, value));
			}

			logger.info("Retorna uma lista com os registros encontrados na consulta");
			criteria.add(Expression.between(column, new Date(start.getTime()), new Date(end.getTime())));
			List list = criteria.list();

			return list;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();

		}
	}

	public List<T> findCriteriaParamsAndBetweenValue(Map<String, Object> params, String column, Object first,
			Object last) throws DAOException {

		try {

			logger.info("conecta no banco de dados");
			session = HibernateUtil.getInstance();

			logger.info(
					"Definie o POJO correspondete a entidade persistente no banco de dados: " + this.persistentClass);
			Criteria criteria = session.createCriteria(this.persistentClass);

			Set s = params.entrySet();

			Iterator it = s.iterator();

			logger.info("Constrói a lista de parâmetros usadas na consulta ");
			while (it.hasNext()) {

				Map.Entry m = (Map.Entry) it.next();
				String key = (String) m.getKey();
				Object value = m.getValue();
				logger.info("Adiciona as restrições na consulta HQL " + "coluna: " + m.getKey() + " - valor: "
						+ m.getValue());
				criteria.add(Restrictions.like(key, value));
			}

			logger.info("Retorna uma lista com os registros encontrados na consulta");
			criteria.add(Expression.between(column, first, last));
			List list = criteria.list();

			return list;

		} catch (Exception e) {

			logger.error("erro ao tentar gravar o registro");
			throw new DAOException("Erro ao tentar gravar o registro: ", e);

		} finally {

			logger.info("Encerra a conexão");
			session.close();

		}
	}

}
