package br.com.albovieira.boaviagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ConfiguracoesActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.preferencias);
    }
}
