package com.example.centralseguranca;

import java.util.List;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AtualizaDados extends Service
{
	private static final String TAG = "AtualizaDados";
	//private boolean isRunning  = false;
	private Runnable mRunnable;

	BancoDados bd;
	protected int n_ocorrencias = -1;

	@Override
	public void onCreate() 
	{
		Log.i(TAG, "Service onCreate");
		//isRunning = true;
		bd = new BancoDados();
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
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext())
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(ocorrencia.getTitulo())
		.setContentText(ocorrencia.getDescricao());
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
        	int n_ocorrencias_updt = bd.num_ocorrencias();
			if(n_ocorrencias_updt != -1) //Nao adquiriou no update
			{
				if(n_ocorrencias == -1) //Nao tinha o valor antes mas adquiriou no update
				{
					n_ocorrencias = n_ocorrencias_updt;
				}
				else if(n_ocorrencias_updt > n_ocorrencias)
				{
					List<?> novas_ocorrencias = bd.getOcorrencias(0, n_ocorrencias_updt - n_ocorrencias);
					for(int i = 0 ; i < novas_ocorrencias.size() ; i++)
					{
						notifica((Ocorrencia)novas_ocorrencias.get(i));
					}
					n_ocorrencias = n_ocorrencias_updt;
				}
			}
			return null;
        }

    }
}
