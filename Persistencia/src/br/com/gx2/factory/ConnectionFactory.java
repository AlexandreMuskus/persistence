package br.com.gx2.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gx2.factory.dao.exception.DAOException;


public class ConnectionFactory {

	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String JDBC_URL = "jdbc:mysql://localhost/treinamento";
	private static String JDBC_USER = "root";
	private static String JDBC_PASSWORD = "";

	public static Connection getConnection() throws DAOException {

		try {
			Class.forName(JDBC_DRIVER);
			return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		} catch (ClassNotFoundException e) {
			throw new DAOException("Driver JDBC não encontrado! ", e.getCause());
		} catch (SQLException e) {
			throw new DAOException("Erro(" + e.getSQLState() + ") na conexão com Banco de Dados! ", e.getCause());
		}
	}
	
	private static void close(Connection conn,
            PreparedStatement pstm,
            ResultSet rs) throws DAOException {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
	
	public static void closeConnection(Connection conn) throws DAOException {
        close(conn, null, null);
    }

    public static void closeConnection(Connection conn,
            PreparedStatement pstm) throws DAOException {
        close(conn, pstm, null);
    }

    public static void closeConnection(Connection conn,
            PreparedStatement pstm,
            ResultSet rs) throws DAOException {
        close(conn, pstm, rs);
    }

    
	
	

}
