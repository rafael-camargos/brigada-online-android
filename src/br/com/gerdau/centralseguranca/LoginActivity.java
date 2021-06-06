package br.com.gerdau.centralseguranca;

import br.com.centralseguranca.R;
import br.com.gerdau.centralseguranca.ContratoBD.FeedUsuarios;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements OnClickListener 
{
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUser;
	private String mPassword;

	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private CheckBox salvarCred;
	private ConexaoBD bd;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Arir Location
		setTitle("Entrar");
		// Set up the login form.
		mUserView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() 
		{
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) 
			{
				if (id == R.id.login || id == EditorInfo.IME_NULL) 
				{
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		salvarCred = (CheckBox) findViewById(R.id.checkCredenciais);
		salvarCred.setChecked(true);
		findViewById(R.id.sign_in_button).setOnClickListener(this);

		bd = new ConexaoBD();
		bd.iniciarBD();

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

		mUser = sharedPref.getString(FeedUsuarios.COL_LOGIN, "");
		mPassword = sharedPref.getString(FeedUsuarios.COL_SENHA, "");
		
		//Log.e("bundle bool", getIntent().getExtras().getBoolean("logout", false) ? "sim" : "não");
		if(!mUser.matches(""))
		{
			mUserView.setText(mUser);
			mPasswordView.setText(mPassword);
			if(bd.conectado())
			{
				if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("logout"))
				{
					
				}
				else
					attemptLogin();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() 
	{
		if (mAuthTask != null) 
		{
			return;
		}

		// Reset errors.
		mUserView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUser = mUserView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		// Check for connection
		if(!bd.conectado())
		{
			if(!bd.abrirBD())
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				alertDialogBuilder.setTitle("Não é possível logar")
				.setMessage("Falha ao conectar com o servidor")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() 
				{public void onClick(DialogInterface dialog,int id){}});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				return;
			}
			else
				attemptLogin();
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) 
		{
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} 
		else if (mPassword.length() < 3) 
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUser)) 
		{
			mUserView.setError(getString(R.string.error_field_required));
			focusView = mUserView;
			cancel = true;
		}

		if (cancel) 
		{
			// There was an error; don't attempt login and focus the first form field with an error.
			focusView.requestFocus();
		} 
		else 
		{
			// Show a progress spinner, and kick off a background task to perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);

			if(salvarCred.isChecked())
			{
				SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
				Editor editor = sharedPref.edit();
				editor.putString(FeedUsuarios.COL_LOGIN, mUserView.getText().toString());
				editor.putString(FeedUsuarios.COL_SENHA, mPasswordView.getText().toString());
				editor.commit();
			}
		}
	}

	//Shows the progress UI and hides the login form.
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) 
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very easy animations. If available, use these APIs to fade-in the progress spinner.

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() 
			{
				@Override
				public void onAnimationEnd(Animator animation) 
				{
					mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() 
			{
				@Override
				public void onAnimationEnd(Animator animation) 
				{
					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} 
		else 
		{
			// The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> 
	{
		@Override
		protected Integer doInBackground(Void... params) 
		{
			return bd.testarLogin(mUser, mPassword);				
		}

		@Override
		protected void onPostExecute(final Integer resultado) 
		{
			mAuthTask = null;
			showProgress(false);
			
			switch(resultado)
			{
			case 0:
				mUserView.setError(getString(R.string.error_invalid_user));
				mUserView.requestFocus();
				break;
				
			case 1:
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
				break;
				
			case 2:
				bd.setarUsuario(bd.buscaUsuario(mUser));
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}

		@Override
		protected void onCancelled() 
		{
			mAuthTask = null;
			showProgress(false);
		}
	}

	@Override
	public void onClick(View arg0) 
	{
		switch(arg0.getId())
		{
		case R.id.sign_in_button:
			attemptLogin();
			break;
		}
	}
}