package com.example.centralseguranca;

public class Usuario 
{
	private String nome, login, senha, tel;
	private int cp, status, tipo;
	
	public Usuario(String nome, String login, String senha, String tel, int cp, int status, int tipo)
	{
		this.setNome(nome);
		this.setLogin(login);
		this.setSenha(senha);
		this.setTel(tel);
		this.cp = cp;
		this.status = status;
		this.tipo = tipo;
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

	public String getSenha() 
	{
		return senha;
	}

	public void setSenha(String senha) 
	{
		this.senha = senha;
	}
}
