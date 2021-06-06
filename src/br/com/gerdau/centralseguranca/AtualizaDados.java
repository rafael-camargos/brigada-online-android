package br.com.gerdau.centralseguranca;

import java.util.List;

import br.com.centralseguranca.R;
import br.com.gerdau.centralseguranca.ContratoBD.FeedOcorrencias;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AtualizaDados extends Service
{
	private static final String TAG = "AtualizaDados";
	//private boolean isRunning  = false;
	private Runnable mRunnable;

	ConexaoBD bd;
	protected int n_ocorrencias = -1;

	@Override
	public void onCreate() 
	{
		Log.i(TAG, "Service onCreate");
		//isRunning = true;
		bd = new ConexaoBD();
		bd.iniciarBD();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.i(TAG, "Service onStartCommand");

		final Handler mHandler = new Handler();
		mRunnable = new Runnable() //
		{
			@Override
			public void run() 
			{
				new lerBD().execute();
				mHandler.postDelayed(mRunnable, 1000);
			}
		};
		mHandler.post(mRunnable);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) 
	{
		Log.i(TAG, "Service onBind");
		return null;
	}

	@Override
	public void onDestroy() 
	{
		//isRunning = false;
		Log.i(TAG, "Service onDestroy");
	}

	private void notifica(Ocorrencia ocorrencia)
	{
		Intent intent = new Intent(this, OcorrenciaActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", Integer.toString(ocorrencia.getId()));

		intent.putExtras(bundle); //Put your id to your next Intent

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext())
		.setSmallIcon(R.drawable.cap_vermelho)
		.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cap_vermelho))
		.setContentTitle(ocorrencia.getTitulo())
		.setContentText(ocorrencia.getDescricao())
		.setVibrate(new long[] {1000, 1000, 1000})
		.setLights(Color.RED, 2000, 2000)
		.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
		.setPriority(Notification.PRIORITY_MAX)
		.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
		.setAutoCancel(true);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(ocorrencia.getId(), mBuilder.build());
	}
	private class lerBD extends AsyncTask<Void, Void, Void> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			lerOcorrencias();

			return null;
		}

		public void lerOcorrencias() 
		{
			int n_ocorrencias_updt = bd.tam_tbl(FeedOcorrencias.NOME_TBL);

			if(n_ocorrencias_updt != -1) //Testa conexão
			{
				if(n_ocorrencias == -1 || n_ocorrencias_updt < n_ocorrencias) //Nao tinha o valor antes mas adquiriou no update OU ocorrência apagada
					n_ocorrencias = n_ocorrencias_updt;
				else if(n_ocorrencias_updt > n_ocorrencias)
				{
					List<?> novas_ocorrencias = bd.getLista(0, n_ocorrencias_updt - n_ocorrencias , true);
					for(int i = 0 ; i < novas_ocorrencias.size() ; i++)
					{
						notifica((Ocorrencia)novas_ocorrencias.get(i));
					}
					n_ocorrencias = n_ocorrencias_updt;
				}
			}
		}

		//BancoDadosInterno mDBInterno = new BancoDadosInterno(getBaseContext());
	}
}
