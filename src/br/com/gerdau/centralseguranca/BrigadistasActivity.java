package br.com.gerdau.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;


import br.com.centralseguranca.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BrigadistasActivity extends Activity implements OnItemClickListener 
{
	private ListView listBrigadistas;
	private ArrayList<Usuario> usuarios;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brigadistas);

		listBrigadistas = (ListView) findViewById(R.id.listBrigadistas);
		listBrigadistas.setOnItemClickListener(this);
		setarBrigadistas();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brigadistas, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	public void setarBrigadistas()
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		ConexaoBD bd = new ConexaoBD();
		
		usuarios = Usuario.organizarLista((ArrayList<Usuario>) bd.getLista(0, 0, false));
		for(int i = 0 ; i < usuarios.size() ; i++)
		{
			HashMap<String,String> aux = new HashMap<String, String>();
			aux.put("Nome", ((Usuario)usuarios.get(i)).getNome());
			aux.put("Telefone", ((Usuario)usuarios.get(i)).getTel());
			aux.put("Status", Usuario.convStatus(((Usuario)usuarios.get(i)).getStatus()));
			data.add(aux);
		}
		AdapterUsuario adapter = new AdapterUsuario(usuarios, this, data, R.layout.layout_usuario, new String[] {"Nome", "Status"}, new int[] {R.id.text_nome, R.id.text_status});
		listBrigadistas.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, final View arg1, final int position, long arg3) 
	{
		if(usuarios != null)
		{
			String nome = ((Usuario)usuarios.get(position)).getNome();
			final String numero_tel = ((Usuario)usuarios.get(position)).getTel();
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					switch (which)
					{
					case DialogInterface.BUTTON_POSITIVE:		        	
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + numero_tel));
						startActivity(intent);
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Deseja ligar agora para " + nome + "? Tel.: " + numero_tel).setPositiveButton("Sim", dialogClickListener)
			.setNegativeButton("Não", dialogClickListener).show();
		}
	}
}
