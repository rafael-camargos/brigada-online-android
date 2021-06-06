package br.com.gerdau.centralseguranca;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import br.com.gerdau.centralseguranca.ContratoBD.FeedOcorrencias;
import br.com.gerdau.centralseguranca.ContratoBD.FeedParticipacao;
import br.com.gerdau.centralseguranca.ContratoBD.FeedUsuarios;

public class ConexaoBD 
{
	private static BancoDados bd;
	private static Usuario user;

	public void iniciarBD()
	{
		bd = new BancoDados();
	}
	public boolean abrirBD() 
	{
		return bd.abrirBD();
	}

	public int tam_tbl(String tabela)
	{
		int linhas_tbl = -1;

		try
		{
			ResultSet rs = bd.execQuery("SELECT COUNT(*) FROM " + tabela);
			if(rs != null)
			{
				rs.first();
				linhas_tbl = rs.getInt(1);
				return linhas_tbl;
			}
			else
				return -1;
		}
		catch(SQLException exception)
		{
			return -1;
		}
	}

	public boolean conectado()
	{
		if(tam_tbl(FeedUsuarios.NOME_TBL) == -1)
			return false;
		else
			return true;
	}



	public List<?> getLista(int start, int size, boolean ocorr_user)
	{
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try
		{
			if(conectado())
			{
				ResultSet rs = bd.execQuery(ocorr_user ? "SELECT * FROM " + FeedOcorrencias.NOME_TBL + " ORDER BY id DESC" : "SELECT * FROM " + FeedUsuarios.NOME_TBL);
				int rowcount = 0;
				if (rs.last()) 
				{
					rowcount = rs.getRow();
				}
				rs.first();
				for(int i = start ; i < (size == 0 ? rowcount : size) ; i++)
				{
					if(ocorr_user)
						ocorrencias.add(new Ocorrencia(rs.getInt("id"), rs.getInt("pr"), rs.getString("gravidade"), rs.getString("descricao"), rs.getTimestamp("dth"), rs.getTimestamp("dth_fech")));
					else
						usuarios.add(new Usuario(rs.getString("nome"), rs.getString("login"), rs.getString("tel"), rs.getInt("np"), rs.getInt("status"), rs.getInt("tipo")));
					rs.next();
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ocorr_user ? ocorrencias : usuarios;
	}

	public Ocorrencia getOcorrencia(int id)
	{
		try
		{
			if(conectado())
			{
				ResultSet rs = bd.execQuery("SELECT * FROM " + FeedOcorrencias.NOME_TBL + " WHERE id = " + id);

				if(rs.first())
				{
				Log.e("gravidade", rs.getString("gravidade"));
					return new Ocorrencia(rs.getInt("id"), rs.getInt("pr"), rs.getString("gravidade"), rs.getString("descricao"), rs.getTimestamp("dth"), rs.getTimestamp("dth_fech"));
				
				}
			}	
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return null;
	}

	public Usuario buscaUsuario(String login)
	{
		try 
		{
			ResultSet rs = bd.execQuery("SELECT * FROM " + FeedUsuarios.NOME_TBL + " WHERE login = '" + login + "'");
			rs.first();
			return new Usuario(rs.getString("nome"), rs.getString("login"), rs.getString("tel"), rs.getInt("np"), rs.getInt("status"), rs.getInt("tipo"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	public Usuario getUsuario()
	{
		return user;
	}
	public ArrayList<ArrayList<String>> getTabela(int start, int size, String tabela, String[] colunas, boolean asc_desc)
	{
		ArrayList<ArrayList<String>> lista = new ArrayList<ArrayList<String>>();
		try
		{
			ResultSet rs = bd.execQuery("SELECT * FROM " + tabela + " ORDER BY id " + (asc_desc ? "ASC" : "DESC"));
			int rowcount = 0;
			if (rs.last()) 
				rowcount = rs.getRow();

			rs.first();
			for(int i = start ; i < (size == 0 ? rowcount : size) ; i++)
			{
				ArrayList<String> coluna = new ArrayList<String>();
				int j = 0;
				while(j < colunas.length)
				{
					coluna.add(rs.getString(colunas[j]));
					j++;
				}
				lista.add(coluna);
				rs.next();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return lista;
	}

	int testarLogin(String login, String senha)
	{
		try
		{
			ResultSet rs = bd.execQuery("SELECT * FROM " + FeedUsuarios.NOME_TBL);

			while(rs.next())
			{
				if(login.matches(rs.getString(FeedUsuarios.COL_LOGIN)))
				{
					if(senha.matches(rs.getString(FeedUsuarios.COL_SENHA)))
						return 2; //Login e senha certo
					else
						return 1; //Login certo e senha errada
				}

			}
			return 0; //Login errado

		}
		catch(Exception e)
		{
			return -1;
		}
	}
	boolean participarOcorrencia(int id_ocorrencia, boolean participando)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		return bd.execUpdate("INSERT INTO " + 
				FeedParticipacao.NOME_TBL + " (" + FeedParticipacao.COL_ID_USER + ", " +
				FeedParticipacao.COL_ID_OCORR + ", " + FeedParticipacao.COL_PARTICIP + ", " + 
				FeedParticipacao.COL_DTH + ") VALUES (" + 
				user.getNp() + ", " + id_ocorrencia + ", " + (participando ? 1:0) + ", " + "'" + format.format(Calendar.getInstance().getTime()) + "'" + ")");
	}

	int participandoOcorrencia(int np_usuario, int id_ocorrencia)
	{
		try
		{
			ResultSet rs = bd.execQuery("SELECT * FROM " + FeedParticipacao.NOME_TBL + " WHERE " +
					FeedParticipacao.COL_ID_OCORR + " = " + id_ocorrencia + " AND " + 
					FeedParticipacao.COL_ID_USER + " = " + np_usuario + " ORDER BY " + 
					FeedParticipacao.COL_DTH + " DESC");

			if(rs.first())
				return rs.getInt(FeedParticipacao.COL_PARTICIP);
			else
				return 0;
		}
		catch(Exception e)
		{
			return -1;
		}

	}
	void setarUsuario(Usuario user)
	{
		Log.e("Setando user", user.getNome());
		ConexaoBD.user = user;
	}
}
