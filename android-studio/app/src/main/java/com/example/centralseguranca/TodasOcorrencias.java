package com.example.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TodasOcorrencias extends Activity implements OnItemClickListener 
{
	private ListView listOcorrencias;
	public ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todas_ocorrencias);

		listOcorrencias = (ListView)findViewById(R.id.listTodasOcorrencias);
		listOcorrencias.setOnItemClickListener(this);

		
		new lerOcorrencias().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todas_ocorrencias, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
	}

	private class lerOcorrencias extends AsyncTask<Void, Void, Void> 
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(TodasOcorrencias.this);
			pDialog.setMessage("Carregando ocorrências...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			BancoDados bd = new BancoDados();
			int n_ocorrencias_updt = bd.num_ocorrencias();

			if(n_ocorrencias_updt != -1) //Nao adquiriou nada
			{
				pDialog.dismiss();

				List<?> novas_ocorrencias = new ArrayList<Ocorrencia>();
				novas_ocorrencias = bd.getOcorrencias(0, n_ocorrencias_updt);
				for(int i = 0 ; i < novas_ocorrencias.size() ; i++)
				{
					HashMap<String,String> aux = new HashMap<String, String>();
					aux.put("ID", "" + ((Ocorrencia)novas_ocorrencias.get(i)).getId());
			        aux.put("Ocorrencia", ((Ocorrencia)novas_ocorrencias.get(i)).getTitulo());
			        data.add(aux);
				}		
			}
			else
			{
				//Erro ao listar ocorrências
			}
			pDialog.dismiss();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{
			SimpleAdapter adapter = new SimpleAdapter(TodasOcorrencias.this, data, R.layout.layout_ocorrencia, new String[] {"ID", "Ocorrencia"}, new int[] {R.id.id_ocorrencia, R.id.texto_ocorrencia});
			listOcorrencias.setAdapter(adapter);
		    
			super.onPostExecute(result);
		}
	}

}
