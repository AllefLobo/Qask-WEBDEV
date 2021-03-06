package br.ufc.webdev.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AmigoDAO {

	private static final String PESSOA_ID = "id";
	private static final String PESSOA_NOME = "nome";
	private static final String PESSOA_EMAIL = "email";
	private static final String PESSOA_SENHA = "senha";
	
	private Connection connection;
	 
	public AmigoDAO(Connection connection){
		this.connection = connection;
	}
	
	private Pessoa bindAmigo(ResultSet rs) throws SQLException {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(rs.getInt(PESSOA_ID));
		pessoa.setNome(rs.getString(PESSOA_NOME));
		pessoa.setEmail(rs.getString(PESSOA_EMAIL));
		pessoa.setSenha(rs.getString(PESSOA_SENHA));
		return pessoa;
	}
	
	
	 
	public List<Pessoa> getAll(int id) throws SQLException{
		List<Pessoa> amigos = new ArrayList<Pessoa>();
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try {
			stmt =	connection.prepareStatement("select * from amigoDe where id_pessoa = ? ");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			PessoaDAO dao = new PessoaDAO(connection);
			while(rs.next()){
				Pessoa amigo = dao.findById(rs.getInt("id_amigo"));
				amigos.add(amigo);
			}
			return amigos;
		} catch (SQLException e) {
			throw new RuntimeException("Nao foi possivel listar os amigos");
		} finally {
			closeOpenResources(rs, stmt);
		}
	}

	public boolean authenticate( int id_pessoa, int id_amigo ) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement("select * from amigoDe where id_pessoa = ? and id_amigo = ?");
			stmt.setInt(1, id_pessoa);
			stmt.setInt(2, id_amigo);
			rs = stmt.executeQuery();
			return rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeOpenResources(rs, stmt);
		}
		return false;
	}
	
	public void adiciona(int id_pessoa, int id_amigo) throws SQLException{
		PreparedStatement stmt = null;
		String sql = "insert into amigoDe(id_pessoa, id_amigo) values (?,?)";
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setInt(1, id_pessoa);
			stmt.setInt(2, id_amigo);
			
			stmt.execute();

		} catch (SQLException e) {
			throw new RuntimeException("Nao foi possivel adicionar o amigo");
		} finally {
				closeOpenResources(stmt);
		}
	}
	
	public void remove(int id_pessoa, int id_amigo) {
		PreparedStatement stmt = null;
		String sql = "delete from amigoDe where id_pessoa = ? and id_amigo = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id_pessoa);
			stmt.setInt(2, id_amigo);
			stmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException("Nao foi possivel remover o amigo");
		} finally {
			closeOpenResources(stmt);
		}
	}
	

	
	private void closeOpenResources(PreparedStatement stmt){
		closeOpenResources(null, stmt);
	}
	
	private void closeOpenResources(ResultSet rs, PreparedStatement stmt){
		try { if (rs != null) rs.close(); } catch (Exception e) {};
	    try { if (stmt != null) stmt.close(); } catch (Exception e) {};
	}
}
