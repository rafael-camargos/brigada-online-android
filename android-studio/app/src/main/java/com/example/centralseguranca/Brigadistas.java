package com.example.centralseguranca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Brigadistas extends Activity implements OnItemClickListener 
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
	    String[] titleArray = new String[]{"Fulano 1", "Ciclano", "Beltrano", "Ciclano 2"};
	    String[] subItemArray = new String[]{"(37) 998650217", "(37) 998650217", "(37) 998650217", "(37) 998650217"};
	    for(int i=0;i<titleArray.length;i++){
	        HashMap<String,String> datum = new HashMap<String, String>();
	        datum.put("RouterName", titleArray[i]);
	        datum.put("RouterIP", subItemArray[i]);
	        data.add(datum);
	    }
	    SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"RouterName", "RouterIP"}, new int[] {android.R.id.text1, android.R.id.text2});
	    listBrigadistas.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		        switch (which)
		        {
		        case DialogInterface.BUTTON_POSITIVE:
		        	
		        	String number = "23454568678";
		    	    Intent intent = new Intent(Intent.ACTION_CALL);
		    	    intent.setData(Uri.parse("tel:" +number));
		    	    startActivity(intent);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Deseja ligar agora para Fulano? Tel.: ").setPositiveButton("Sim", dialogClickListener)
		    .setNegativeButton("Não", dialogClickListener).show();
	}
}
