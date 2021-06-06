package br.com.gerdau.centralseguranca;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Ocorrencia 
{
	private int id, pr;
	private boolean situacao;
	private String gravidade, descricao;
	private Date data, data_fec;
	
	Ocorrencia(int id, int pr, String gravidade, String descricao, Date data, Date data_fec)
	{
		this.id = id;
		this.setPr(pr);
		this.gravidade = gravidade;
		this.setDescricao(descricao);
		this.setData(data);
		this.data_fec = data_fec;
	}

	public String getTitulo() 
	{
		return "Ocorrência " + gravidade + " no PR " + pr;
	}
	public int getId()
	{
		return id;
	}

	public String getGravidade() 
	{
		return gravidade;
	}
	
	public String getDescricao() 
	{
		return descricao;
	}

	public void setDescricao(String descricao) 
	{
		this.descricao = descricao;
	}

	public int getPr() 
	{
		return pr;
	}

	public void setPr(int pr) 
	{
		this.pr = pr;
	}

	public Date getData() 
	{
		return data;
	}
	public String getDataString()
	{
		SimpleDateFormat spf = new SimpleDateFormat("d/M/yyyy, HH:mm", Locale.getDefault());
		return spf.format(data);
	}

	public void setData(Date data) 
	{
		this.data = data;
	}

	public String getSituacao() 
	{
		return data_fec == null ? "Em aberto" : "Finalizada";
	}
}
