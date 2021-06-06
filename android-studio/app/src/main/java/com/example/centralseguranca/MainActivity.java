package com.example.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener 
{
	private boolean logado = true; //teste
	private Button bVerBrigadistas;
	private Button bVerOcorrencias;
	private ListView listOcorrencias;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			

			bVerBrigadistas = (Button) findViewById(R.id.bVerBrigadistas);
			bVerBrigadistas.setOnClickListener(this);

			bVerOcorrencias = (Button) findViewById(R.id.bVerOcorrencias);
			bVerOcorrencias.setOnClickListener(this);

			listOcorrencias = (ListView)findViewById(R.id.listOcorrencias);
			listOcorrencias.setOnItemClickListener(this);
			
			//inicia com o aplicativo (teste)
			Intent service_atualiza = new Intent(this, AtualizaDados.class);
			startService(service_atualiza);
			
			//bd = new BancoDados(); //Passar variavel para outras classes
			setarOcorrencias();
			//conectarBD();
			
			if(!logado)
				finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) 
	{
		switch(arg0.getId())
		{
		case R.id.bVerBrigadistas:
			Intent intentBrig = new Intent(this, BrigadistasActivity.class);
			startActivity(intentBrig);
			break;

		case R.id.bVerOcorrencias:
			Intent intentOc = new Intent(this, TodasOcorrencias.class);
			startActivity(intentOc);
			break;
		}
	}

	public void setarOcorrencias()
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	    BancoDados bd = new BancoDados();
    	List<?> ultimasOcorrencias = bd.getOcorrencias(0, 3);
	    for(int i = 0 ; i < ultimasOcorrencias.size() ; i++)
	    {
	        HashMap<String, String> aux = new HashMap<String, String>();
	        aux.put("Nome", ((Ocorrencia)ultimasOcorrencias.get(i)).getTitulo());
	        aux.put("ID", "" + ((Ocorrencia)ultimasOcorrencias.get(i)).getId());
	        data.add(aux);
	    }
	    SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, new String[] {"Nome"}, new int[] {android.R.id.text1});
	    listOcorrencias.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent(this, OcorrenciaActivity.class);
		startActivity(intent);
		
		//listOcorrencias.getAdapter().
	}
}
