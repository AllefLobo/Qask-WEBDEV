package br.ufc.webdev.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.ufc.webdev.model.Pessoa;
import br.ufc.webdev.model.PessoaDAO;

@WebServlet("/autentica")
public class AuthenticationController extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// sucesso - index.jsp
		
		// erro - login.jsp
		Connection connection = (Connection) req.getAttribute("connection");
		PessoaDAO dao = new PessoaDAO(connection);
		
		String nome = req.getParameter("login-username");
		String senha = req.getParameter("login-password");
		
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome(nome);
		pessoa.setSenha(senha);

		try {
			if( dao.authenticate(pessoa) ){
				
				System.out.println(dao.authenticate(pessoa));
				HttpSession session = req.getSession();
				pessoa = dao.buscarPessoa(pessoa.getNome());
				session.setAttribute("user", pessoa);
				
				req.getRequestDispatcher("/listarRespostas").forward(req, resp);
						
			}else{
				req.getRequestDispatcher("login.jsp").forward(req, resp);;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
