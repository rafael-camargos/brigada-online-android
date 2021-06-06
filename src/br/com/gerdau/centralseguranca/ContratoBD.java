package br.com.gerdau.centralseguranca;

import android.provider.BaseColumns;

public final class ContratoBD 
{
    private ContratoBD() {}

    /* Inner class that defines the table contents */
    public static class FeedOcorrencias implements BaseColumns 
    {
        public static final String NOME_TBL = "ocorrencias";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final int OCORR_ABERTA = 1;
        public static final int OCORR_FECHADA = 1;
    }
    
    public static class FeedUsuarios implements BaseColumns 
    {
        public static final String NOME_TBL = "usuarios";
        public static final String COL_NOME = "nome";
        public static final String COL_NP = "np";
        public static final String COL_LOGIN = "login";
        public static final String COL_SENHA = "senha";
        public static final String COL_TEL = "tel";
        public static final String COL_STATUS = "status";
        public static final String COL_TIPO = "tipo";
        
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

        public static final String STRING_ST_DESC = "Desconhecido";
        public static final String STRING_ST_F_USINA = "Fora da Usina";
        public static final String STRING_ST_USINA = "Na Usina";
        
        public static final int ST_DESC = 0;
        public static final int ST_F_USINA = 1;
        public static final int ST_USINA = 2;
        
        public static final int TIPO_COMUM = 0;
        public static final int TIPO_BRIGADISTA = 1;
        public static final int TIPO_ADMIN = 2;
    }
    
    public static class FeedParticipacao implements BaseColumns 
    {
        public static final String NOME_TBL = "usuarios_ocorrencias";
        public static final String COL_ID_USER = "usuario_id";
        public static final String COL_ID_OCORR = "ocorrencia_id";
        public static final String COL_PARTICIP = "participando";
        public static final String COL_DTH = "dth";
    }
}