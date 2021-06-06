package br.com.gerdau.centralseguranca;

import java.util.List;
import java.util.Map;

import br.com.centralseguranca.R;
import br.com.gerdau.centralseguranca.ContratoBD.FeedUsuarios;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;


public class AdapterUsuario extends SimpleAdapter
{
	//private List<? extends Map<String, ?>> data;
	private List<?> usuarios;
	public AdapterUsuario(List<?> usuarios, Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) 
	{
		super(context, data, resource, from, to);
		//this.data = data;
		this.usuarios = usuarios;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = super.getView(position, convertView, parent);
		if(((Usuario)usuarios.get(position)).getTipo() == FeedUsuarios.TIPO_BRIGADISTA)
		{
			if(((Usuario)usuarios.get(position)).getStatus() == FeedUsuarios.ST_USINA)
				((ImageView)view.findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_vermelho);
			else
				((ImageView)view.findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_vermelho_cinza);
		}
		else
		{
			if(((Usuario)usuarios.get(position)).getStatus() == FeedUsuarios.ST_USINA)
				((ImageView)view.findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_amarelo);
			else
				((ImageView)view.findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_amarelo_cinza);
		}
		return view;
	}

	@Override
	public int getCount() 
	{
		return super.getCount();
	}
}
