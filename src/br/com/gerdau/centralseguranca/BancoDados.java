package br.com.gerdau.centralseguranca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.os.StrictMode;
import android.util.Log;

public class BancoDados 
{
	private Connection connection;

	public BancoDados()
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		abrirBD();
	}

	public void fecharBD()
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

	public boolean abrirBD() 
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://sql10.freesqldatabase.com/sql10223867", "sql10223867", "ycH2B4Ijab");
			
			Log.e("Conectado", "BD");
			return true;

		}
		catch (Exception e1) 
		{
			//String trace = e1.getStackTrace().toString();
			//Log.e("DB", "SQL Exception" + trace, e1);
			return false;
		}
	}

	public ResultSet execQuery(String query)
	{
		Log.w("Tentativa de executar query", query);
		if(connection != null)
		{
			PreparedStatement pst;
			try 
			{
				pst = connection.prepareStatement(query);
				return pst.executeQuery();
			} 
			catch (SQLException e) 
			{
				Log.e("Erro ao executar query: ", query);
				Log.e("Erro: ", e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}

	public boolean execUpdate(String query) 
	{
		Log.w("Tentativa de executar query", query);
		PreparedStatement preapred;
		try 
		{
			preapred = connection.prepareStatement(query);
			preapred.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			Log.e("Erro ao executar query: ", query);
			Log.e("Erro: ", e.getMessage());
			return false;
		}
	}

	

}
