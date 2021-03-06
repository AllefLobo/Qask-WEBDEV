package br.ufc.webdev.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.ufc.webdev.model.Pergunta;
import br.ufc.webdev.model.PerguntaDAO;
import br.ufc.webdev.model.Pessoa;

import com.mysql.jdbc.Connection;
@WebServlet(urlPatterns={"/listaPergunta"})
public class listaPergunta extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection connection = (Connection) req.getAttribute("connection");
		PerguntaDAO perguntaDAO = new PerguntaDAO(connection);
		List<Pergunta> listaTodasPerguntas = new ArrayList<Pergunta>();
		
		HttpSession session = ((HttpServletRequest) req).getSession(true);
		
		Pessoa pessoa = (Pessoa) session.getAttribute("user");
		//ainda acho q n é esse
		
		listaTodasPerguntas = perguntaDAO.pegarPerguntasDePessoa(pessoa);
		List<Pergunta> perguntasNaoRespondidas = new ArrayList<Pergunta>();
		//ainda acho q n é esse
		
		for(Pergunta pergunta : listaTodasPerguntas){
			 if(pergunta.getIdResposta() == 0){				
				perguntasNaoRespondidas.add(pergunta);
			}
		}
		
		req.setAttribute("perguntas", perguntasNaoRespondidas);
		
		RequestDispatcher rd = req.getRequestDispatcher("perguntas.jsp");
		rd.forward(req, resp);
	}
}
