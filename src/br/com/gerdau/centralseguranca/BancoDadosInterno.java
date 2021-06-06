package br.com.gerdau.centralseguranca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDadosInterno extends SQLiteOpenHelper 
{
	
	
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DBBrigadaInterno.db";

    public BancoDadosInterno(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) 
    {
    	db.execSQL("CREATE TABLE usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(30), cp INT, login VARCHAR(20), senha VARCHAR(20), tel VARCHAR(20), status TINYINT, tipo TINYINT)");
        db.execSQL("CREATE TABLE ocorrencias (id INT AUTO_INCREMENT PRIMARY KEY, pr INT, gravidade VARCHAR(20), descricao VARCHAR(200), dth DATETIME)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE usuarios");
        db.execSQL("DROP TABLE ocorrencias");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}