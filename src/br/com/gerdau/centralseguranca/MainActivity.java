package br.com.gerdau.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.centralseguranca.R;
import br.com.gerdau.centralseguranca.ContratoBD.FeedUsuarios;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener 
{
	private ListView listBrigadistas;
	private ListView listOcorrencias;
	private List<?> ultimasOcorrencias;
	ConexaoBD bd;
	private List<Usuario> usuarios;
	private List<Usuario> brigadistas_usina;


	private String[] drawerListViewItems;
	private ListView drawerListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		drawerListViewItems = getResources().getStringArray(R.array.menu_esquerdo);
		drawerListView = (ListView) findViewById(R.id.list_left);
		drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, drawerListViewItems));
		drawerListView.setOnItemClickListener(this);

		listBrigadistas = (ListView)findViewById(R.id.listBrigadistasMain);

		listOcorrencias = (ListView)findViewById(R.id.listOcorrenciasMain);
		listOcorrencias.setOnItemClickListener(this);


		FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
		.withDrawable(getResources().getDrawable(R.drawable.refresh))
		.withButtonColor(Color.WHITE)
		.withGravity(Gravity.BOTTOM | Gravity.CENTER)
		.withMargins(0, 0, 16, 16)
		.create();

		fabButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				new atualizaActivity().execute();
			}
		});

		Intent service_atualiza = new Intent(this, AtualizaDados.class);
		startService(service_atualiza);

		bd = new ConexaoBD(); //Passar variavel para outras classes ?


		((TextView)findViewById(R.id.nome_usuario)).setText(bd.getUsuario().getNome());

		if(bd.getUsuario().getTipo() == FeedUsuarios.TIPO_BRIGADISTA)
			((ImageView)findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_vermelho);
		else
			((ImageView)findViewById(R.id.image_capacete)).setImageResource(R.drawable.cap_amarelo);


		new atualizaActivity().execute();		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class atualizaActivity extends AsyncTask<Void, Void, Void> 
	{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> data_ocorrencias = new ArrayList<HashMap<String, String>>();

		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Carregando registros...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... arg0) 
		{
			ConexaoBD bd = new ConexaoBD();
			//usuarios = new ArrayList<Usuario>();
			usuarios = Usuario.organizarLista((ArrayList<Usuario>) bd.getLista(0, 0, false));

			brigadistas_usina = new ArrayList<Usuario>();

			//ultimasOcorrencias = new ArrayList<String>();
			ultimasOcorrencias = bd.getLista(0, 3, true);

			for(int i = 0 ; i < usuarios.size() ; i++)
			{
				if(((Usuario)usuarios.get(i)).getStatus() == FeedUsuarios.ST_USINA && ((Usuario)usuarios.get(i)).getTipo() == FeedUsuarios.TIPO_BRIGADISTA)
				{
					HashMap<String,String> aux = new HashMap<String, String>();
					aux.put("Nome", ((Usuario)usuarios.get(i)).getNome());
					aux.put("Telefone", ((Usuario)usuarios.get(i)).getTel());
					aux.put("Status", "Na usina");
					data.add(aux);
					brigadistas_usina.add(usuarios.get(i));
				}
			}

			for(int i = 0 ; i < ultimasOcorrencias.size() ; i++)
			{
				HashMap<String, String> aux = new HashMap<String, String>();
				aux.put("Nome", ((Ocorrencia)ultimasOcorrencias.get(i)).getTitulo());
				data_ocorrencias.add(aux);
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{

			AdapterUsuario adapterUsuarios = new AdapterUsuario(brigadistas_usina, MainActivity.this, data, R.layout.layout_usuario, new String[] {"Nome", "Status"}, new int[] {R.id.text_nome, R.id.text_status});
			listBrigadistas.setAdapter(adapterUsuarios);


			SimpleAdapter adapterOcorrencias = new SimpleAdapter(MainActivity.this, data_ocorrencias, android.R.layout.simple_list_item_1, new String[] {"Nome"}, new int[] {android.R.id.text1});
			listOcorrencias.setAdapter(adapterOcorrencias);

			pDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		
		Log.e("id", ""+arg0.getId());
		Log.e("arg2", ""+arg2);
		switch(arg0.getId())
		{
		case R.id.listOcorrenciasMain:
			if(ultimasOcorrencias != null)
			{
				Intent intent = new Intent(this, OcorrenciaActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", "" + ((Ocorrencia)ultimasOcorrencias.get(arg2)).getId());
				//bundle.putString("situacao", "Em aberto");
				//bundle.putString("descricao", ((Ocorrencia)ultimasOcorrencias.get(arg2)).getDescricao());
				//bundle.putString("gravidade", ((Ocorrencia)ultimasOcorrencias.get(arg2)).getGravidade());
				//bundle.putString("pr", "PR " + ((Ocorrencia)ultimasOcorrencias.get(arg2)).getPr());
				//bundle.putString("dth", ((Ocorrencia)ultimasOcorrencias.get(arg2)).getDataString());

				intent.putExtras(bundle); //Put your id to your next Intent
				startActivity(intent);
			}
			else
			{
				//ERRO
			}
			break;
			
		case R.id.list_left:
			switch(arg2)
			{
			case 0:
				Intent intentOc = new Intent(this, TodasOcorrencias.class);
				startActivity(intentOc);
				break;
			case 1:
				Intent intentBrig = new Intent(this, BrigadistasActivity.class);
				startActivity(intentBrig);
				break;
			case 2:
				Intent intentLogin = new Intent(this, LoginActivity.class);
				intentLogin.putExtra("logout", true);
				startActivity(intentLogin);
				finish();
				break;
			}
			break;
		}
	}
}
