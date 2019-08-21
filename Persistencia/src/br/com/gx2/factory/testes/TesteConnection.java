package br.com.gx2.factory.testes;

import java.util.List;

import br.com.gx2.dao.ContatoDAO;
import br.com.gx2.dao.imp.ContatoDAOImp;
import br.com.gx2.entity.Contato;
import br.com.gx2.factory.dao.exception.DAOException;

public class TesteConnection {

	public static void main(String[] args) throws DAOException {
		
		ContatoDAO dao = new ContatoDAOImp();
	   
		List<Contato> contatos = dao.listAll();
	    for (Contato contato : contatos) {
			System.out.println(contato.toString());
		}

	}

}
