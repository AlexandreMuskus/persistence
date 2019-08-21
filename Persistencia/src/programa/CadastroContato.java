package programa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gx2.entity.Contato;
import br.com.gx2.factory.ConnectionFactory;
import br.com.gx2.factory.dao.exception.DAOException;

public class CadastroContato {

	private Connection conn;
	PreparedStatement pstm;
	ResultSet rs;

	public CadastroContato() throws DAOException {

		this.conn = ConnectionFactory.getConnection();
	}

	public void save(Contato contato) throws DAOException, SQLException {

		String sql = "INSERT INTO contatos (nome, idade) VALUES (?,?)";

		pstm = conn.prepareStatement(sql.toString());
		pstm.setString(1, contato.getNome());
		pstm.setInt(2, contato.getIdade());
		pstm.execute();

	}

	public void update(Contato contato) throws DAOException, SQLException {

		String sql = "UPDATE contatos SET nome=?, idade=? WHERE id_contato=?";

		pstm = conn.prepareStatement(sql.toString());
		pstm.setString(1, contato.getNome());
		pstm.setInt(2, contato.getIdade());
		pstm.setInt(3, contato.getIdContato());
		pstm.executeUpdate();

	}

	public void delete(Contato contato) throws DAOException, SQLException {

		String sql = "delete from contatos WHERE id_contato=?";
		pstm = conn.prepareStatement(sql.toString());
		pstm.setInt(1, contato.getIdContato());
		pstm.executeUpdate();

	}

	public List<Contato> findAll() throws DAOException, SQLException {
		String sql = "select * from contatos";
		Contato contato;
		List<Contato> resultado = new ArrayList<Contato>();
		pstm = conn.prepareStatement(sql.toString());
		rs = pstm.executeQuery();

		while (rs.next()) {
			contato = new Contato();
			contato.setIdContato(rs.getInt("id_contato"));
			contato.setNome(rs.getString("nome"));
			contato.setIdade(rs.getInt("idade"));
			resultado.add(contato);

		}

		return resultado;
	}

	public Contato findById(int id) throws DAOException, SQLException {

		String sql = "select * from contatos WHERE id_contato=?";
		Contato contato = new Contato();
	
		pstm = conn.prepareStatement(sql.toString());
		pstm.setInt(1, id);
		rs = pstm.executeQuery();
		
		
		while (rs.next()) {
			
			contato.setIdContato(rs.getInt("id_contato"));
			contato.setNome(rs.getString("nome"));
			contato.setIdade(rs.getInt("idade"));
			

		}

		return contato;
	}

}
