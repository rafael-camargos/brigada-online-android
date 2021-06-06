package br.com.gerdau.centralseguranca;

import br.com.centralseguranca.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class OcorrenciaActivity extends Activity implements OnClickListener 
{
	private TextView textId;
	private TextView textSituacao;
	private TextView textGravidade;
	private TextView textDescricao;
	private TextView textPR;
	private TextView textDTH;

	private TextView textParticipar;
	private ImageButton button_participar;
	private TextView textSair;
	private ImageButton button_sair;

	private boolean participando = false;
	private ConexaoBD bd;

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocorrencia);

		textId = (TextView) findViewById(R.id.text_id);
		textSituacao = (TextView) findViewById(R.id.text_situacao);
		textGravidade = (TextView) findViewById(R.id.text_gravidade);
		textDescricao = (TextView) findViewById(R.id.text_descricao);
		textPR = (TextView) findViewById(R.id.text_pr);
		textDTH = (TextView) findViewById(R.id.text_dth);

		textParticipar = (TextView) findViewById(R.id.textParticiparOc);
		textParticipar.setOnClickListener(this);

		button_participar = (ImageButton) findViewById(R.id.participarOcorrencia);
		button_participar.setOnClickListener(this);

		textSair = (TextView) findViewById(R.id.textSairOc);
		textSair.setOnClickListener(this);

		button_sair = (ImageButton) findViewById(R.id.sairOcorrencia);
		button_sair.setOnClickListener(this);
		
		bd = new ConexaoBD();

		Bundle bundle = getIntent().getExtras();	
		
		if(bundle != null)
		{
			
			id = Integer.parseInt(bundle.getString("id"));
			Ocorrencia ocorrencia = bd.getOcorrencia(id);
			textId.setText(bundle.getString("id"));
			textSituacao.setText(ocorrencia.getSituacao());
			textGravidade.setText(ocorrencia.getGravidade());
			textDescricao.setText(ocorrencia.getDescricao());
			textPR.setText(Integer.toString(ocorrencia.getPr()));
			textDTH.setText(ocorrencia.getDataString());
		}

		

		switch(bd.participandoOcorrencia(bd.getUsuario().getNp(), id))
		{
			case -1:
				//ERRO
				break;
			case 0:
				participando(false);
				break;
			case 1:
				participando(true);
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocorrencia, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) 
	{
		switch(arg0.getId())
		{
		case R.id.participarOcorrencia:
		case R.id.textParticiparOc:
		case R.id.sairOcorrencia:
		case R.id.textSairOc:

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					switch (which)
					{
					case DialogInterface.BUTTON_POSITIVE:
						if(participando)
						{
							participando(false);	
							bd.participarOcorrencia(id, false);
						}
						else
						{
							participando(true);
							bd.participarOcorrencia(id, true);
						}
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Tem certeza que deseja " + (participando ? "sair" : "participar") + " dessa ocorrência?").setPositiveButton("Sim", dialogClickListener)
			.setNegativeButton("Não", dialogClickListener).show();
			break;
		}
	}

	public void participando(boolean particip)
	{
		participando = particip;
		if(particip)
		{			
		    ColorMatrix matrix = new ColorMatrix();
		    matrix.setSaturation((float) 0.5);  //0 means grayscale
		    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
		    button_participar.setColorFilter(cf);
		    button_participar.setImageAlpha(128);   // 128 = 0.5
		    
			button_sair.setColorFilter(null);
			button_sair.setImageAlpha(255);
			

		    button_participar.setClickable(false);
		    textParticipar.setClickable(false);
		    
		    button_sair.setClickable(true);
		    textSair.setClickable(true);
		}
		else
		{
		    ColorMatrix matrix = new ColorMatrix();
		    matrix.setSaturation((float) 0.5);  //0 means grayscale
		    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
		    button_sair.setColorFilter(cf);
		    button_sair.setImageAlpha(128);   // 128 = 0.5
		        
			button_participar.setColorFilter(null);
			button_participar.setImageAlpha(255);
			
		    button_participar.setClickable(true);
		    textParticipar.setClickable(true);
		    
		    button_sair.setClickable(false);
		    textSair.setClickable(false);
		}
	}
}
