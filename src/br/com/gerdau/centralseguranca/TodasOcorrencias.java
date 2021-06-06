package br.com.gerdau.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.centralseguranca.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
	public List<?> ocorrencias;
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
		if(ocorrencias != null)
		{
			Intent intent = new Intent(this, OcorrenciaActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", "" + ((Ocorrencia)ocorrencias.get(arg2)).getId());
			//bundle.putString("situacao", "Em aberto");
			//bundle.putString("descricao", ((Ocorrencia)ocorrencias.get(arg2)).getDescricao());
			//bundle.putString("gravidade", ((Ocorrencia)ocorrencias.get(arg2)).getGravidade());
			//bundle.putString("pr", "PR " + ((Ocorrencia)ocorrencias.get(arg2)).getPr());
			//bundle.putString("dth", ((Ocorrencia)ocorrencias.get(arg2)).getDataString());

			intent.putExtras(bundle); //Put your id to your next Intent
			startActivity(intent);
		}
		else
		{
			//ERRO
		}
	}

	private class lerOcorrencias extends AsyncTask<Void, Void, Void> 
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			pDialog = new ProgressDialog(TodasOcorrencias.this);
			pDialog.setMessage("Carregando ocorrências...");
			pDialog.setCancelable(false);
			pDialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			ConexaoBD bd = new ConexaoBD();
			int n_ocorrencias_updt = bd.tam_tbl("ocorrencias");

			if(n_ocorrencias_updt != -1) //Nao adquiriou nada
			{
				ocorrencias = new ArrayList<Ocorrencia>();
				ocorrencias = bd.getLista(0, n_ocorrencias_updt, true);
				
				//ArrayList<ArrayList<String>> tabela = bd.getTabela(0, n_ocorrencias_updt, "ocorrencias", new String[]{"id","pr","gravidade", "descricao", "dth"}, false);
				for(int i = 0 ; i < ocorrencias.size() ; i++)
				{
					HashMap<String,String> aux = new HashMap<String, String>();
					aux.put("ID", "" + ((Ocorrencia)ocorrencias.get(i)).getId());
					aux.put("Ocorrencia", ((Ocorrencia)ocorrencias.get(i)).getTitulo());
					aux.put("Descr", ((Ocorrencia)ocorrencias.get(i)).getDescricao());
					data.add(aux);
				}		
			}
			else
			{
				//Erro ao listar ocorrências
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{
			pDialog.dismiss();
			SimpleAdapter adapter = new SimpleAdapter(TodasOcorrencias.this, data, R.layout.layout_ocorrencia, new String[] {"Ocorrencia", "Descr"}, new int[] {R.id.text1, R.id.text2});
			listOcorrencias.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}

}
