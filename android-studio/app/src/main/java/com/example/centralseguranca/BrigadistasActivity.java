package com.example.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BrigadistasActivity extends Activity implements OnItemClickListener 
{
	private ListView listBrigadistas;

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

	public void setarBrigadistas()
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	    BancoDados bd = new BancoDados();
    	List<Usuario> usuarios = new ArrayList<Usuario>();
    	usuarios = bd.getUsuarios(0, 5);
	    for(int i = 0 ; i < usuarios.size() ; i++)
	    {
	        HashMap<String,String> aux = new HashMap<String, String>();
	        aux.put("Nome", usuarios.get(i).getNome());
	        aux.put("Telefone", usuarios.get(i).getTel());
	        data.add(aux);
	    }
	    SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Nome", "Telefone"}, new int[] {android.R.id.text1, android.R.id.text2});
	    listBrigadistas.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3) 
	{
		final String nome = ((TextView)(arg1.findViewById(android.R.id.text1))).getText().toString();
		final String numero_tel = ((TextView)(arg1.findViewById(android.R.id.text2))).getText().toString();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		        switch (which)
		        {
		        case DialogInterface.BUTTON_POSITIVE:		        	
		    	    Intent intent = new Intent(Intent.ACTION_CALL);
		    	    intent.setData(Uri.parse("tel:" + numero_tel));
		    	    startActivity(intent);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Deseja ligar agora para " + nome + "? Tel.: " + numero_tel).setPositiveButton("Sim", dialogClickListener)
		    .setNegativeButton("Não", dialogClickListener).show();
	}
}
