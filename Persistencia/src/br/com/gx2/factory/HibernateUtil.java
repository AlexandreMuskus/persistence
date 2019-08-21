package br.com.gx2.factory;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author Ronaldo Prass
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	
	private static Logger logger = Logger.getLogger(HibernateUtil.class);
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory();

		} catch (Throwable t) {
			logger.error("erro ao tentar conectar no banco de dados: ", t);
			throw new ExceptionInInitializerError(t);

		}
	}

	public static Session getInstance() {
		Session session = (Session) threadLocal.get();
		session = sessionFactory.openSession();
		threadLocal.set(session);
		logger.info("Retorna a sessão ativa com o banco de dados");
		return session;
	}
}