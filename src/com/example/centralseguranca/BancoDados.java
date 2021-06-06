package com.example.centralseguranca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import android.os.StrictMode;
import android.util.Log;

public class BancoDados 
{
	private Connection connection;
	
	public BancoDados()
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	public boolean conectarBD()
	{
		if(abrirBD())
		{
			fecharBD();
			return true;
		}
		else 
			return false;
	}

	private void fecharBD() 
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private boolean abrirBD() 
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://sql10.freesqldatabase.com/sql10223867","sql10223867","ycH2B4Ijab");
			return true;

		}
		catch (Exception e1) 
		{
			String trace = e1.getStackTrace().toString();
			Log.e("DB", "SQL Exception" + trace,e1);
			return false;
		}
	}

	public int num_ocorrencias()
	{
		int ocorrencias = -1;
		try
		{
			if(abrirBD())
			{
				PreparedStatement pst = connection.prepareStatement("SELECT COUNT(*) FROM ocorrencias");
				ResultSet rs = pst.executeQuery();
				rs.first();
				ocorrencias = rs.getInt(1);
				fecharBD();
				return ocorrencias;
			}
			else
				return -1;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}
	}

	public List<?> getOcorrencias(int start, int size)
	{
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		try
		{
			if(abrirBD())
			{
				PreparedStatement pst = connection.prepareStatement("SELECT * FROM ocorrencias ORDER BY id DESC");
				ResultSet rs;
				rs = pst.executeQuery();
				rs.first();
				for(int i = start ; i < size ; i++)
				{
					ocorrencias.add(new Ocorrencia(rs.getInt("id"), rs.getInt("pr"), rs.getString("gravidade"), rs.getString("descricao")));
					rs.next();
				}
				fecharBD();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ocorrencias;
	}
	public List<Usuario> getUsuarios(int start, int size)
	{
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try
		{
			if(abrirBD())
			{
				PreparedStatement pst = connection.prepareStatement("SELECT * FROM usuarios");
				ResultSet rs;
				rs = pst.executeQuery();
				rs.first();
				for(int i = start ; i < size ; i++)
				{
					usuarios.add(new Usuario(rs.getString("nome"), rs.getString("login"), rs.getString("senha"), rs.getString("tel"), rs.getInt("cp"), rs.getInt("status"), rs.getInt("tipo")));
					rs.next();
				}
				fecharBD();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return usuarios;
	}
}
