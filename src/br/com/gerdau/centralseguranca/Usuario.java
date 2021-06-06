package br.com.gerdau.centralseguranca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Usuario 
{
	private String nome, login, tel;
	private int np, status, tipo;

	private static HashMap<Integer, String> conversaoStatus;

	public Usuario(String nome, String login, String tel, int np, int status, int tipo)
	{
		this.setNome(nome);
		this.setLogin(login);
		this.setTel(tel);
		this.setNp(np);
		this.setStatus(status);
		this.setTipo(tipo);

		conversaoStatus = new HashMap<Integer, String>();
		conversaoStatus.put(0, "Desconhecido");
		conversaoStatus.put(1, "Fora da Usina");
		conversaoStatus.put(2, "Na Usina");
	}

	public String getNome() 
	{
		return nome;
	}

	public void setNome(String nome) 
	{
		this.nome = nome;
	}

	public String getTel() 
	{
		return tel;
	}

	public void setTel(String tel) 
	{
		this.tel = tel;
	}

	public String getLogin() 
	{
		return login;
	}

	public void setLogin(String login) 
	{
		this.login = login;
	}

	public int getTipo() 
	{
		return tipo;
	}

	public void setTipo(int tipo) 
	{
		this.tipo = tipo;
	}

	public int getStatus() 
	{
		return status;
	}

	public void setStatus(int status) 
	{
		this.status = status;
	}

	public int getNp() 
	{
		return np;
	}

	public void setNp(int np) 
	{
		this.np = np;
	}

	public static ArrayList<Usuario> organizarLista(ArrayList<Usuario> usuarios)
	{
		Collections.sort((ArrayList<Usuario>)usuarios, new Comparator<Usuario>() 
				{
			@Override
			public int compare(Usuario arg0, Usuario arg1) 
			{
				return arg0.nome.compareTo(arg1.getNome());
			}
				});

		Collections.sort((ArrayList<Usuario>)usuarios, new Comparator<Usuario>() 
				{
			@Override
			public int compare(Usuario arg0, Usuario arg1) 
			{
				return arg1.getStatus() - arg0.getStatus();
			}
				});

		Collections.sort((ArrayList<Usuario>)usuarios, new Comparator<Usuario>() 
				{
			@Override
			public int compare(Usuario arg0, Usuario arg1) 
			{
				return arg1.getTipo() - arg0.getTipo();
			}
				});

		return usuarios;
	}

	public static String convStatus(int status)
	{
		return conversaoStatus.get(status);
	}
}
