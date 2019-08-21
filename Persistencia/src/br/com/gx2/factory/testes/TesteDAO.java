package br.com.gx2.factory.testes;

import java.sql.SQLException;
import java.util.List;

import br.com.gx2.dao.ContatoDAO;
import br.com.gx2.dao.imp.ContatoDAOImp;
import br.com.gx2.entity.Contato;
import br.com.gx2.factory.dao.exception.DAOException;
import programa.CadastroContato;

public class TesteDAO {

	public static void main(String[] args) throws DAOException, SQLException {
		
		Contato contato = new Contato();
		contato.setIdContato(23);
		contato.setNome("Ronaldinho Malvadeza");
		contato.setIdade(3333);
		
		ContatoDAO dao = new ContatoDAOImp();
		dao.delete(contato);
		
		//CadastroContato cadastro = new CadastroContato();
		//cadastro.delete(contato);
		/*List<Contato> lista = cadastro.findAll();
		for (Contato valor : lista) {
			System.out.println(valor.toString());
		}*/
		//Contato obj = cadastro.findById(contato.getIdContato());
		//System.out.println(obj.toString());
		

	}

}
