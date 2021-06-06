package com.example.centralseguranca;

import java.util.Date;

public class Ocorrencia 
{
	private int id, pr;
	private String gravidade, descricao;
	private Date data;
	
	Ocorrencia(int id, int pr, String gravidade, String descricao)
	{
		this.id = id;
		this.pr = pr;
		this.gravidade = gravidade;
		this.setDescricao(descricao);
	}

	public String getTitulo() 
	{
		return "Ocorrência " + gravidade + " no PR " + pr;
	}
	public int getId()
	{
		return id;
	}

	public String getDescricao() 
	{
		return descricao;
	}

	public void setDescricao(String descricao) 
	{
		this.descricao = descricao;
	}
	
}
