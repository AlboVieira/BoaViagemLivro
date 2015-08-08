package br.com.albovieira.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class BoaViagemActivity extends Activity {
    private static final String MANTER_CONECTADO = "manter_conectado";
	private EditText usuario;
	private EditText senha;
	private CheckBox manterConectado;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
        
        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
		manterConectado = (CheckBox) findViewById(R.id.manterConectado);

		//se estiver setado nas preferencias para manter conectado , abre a intent dashboard
		SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
		boolean conectado = preferencias.getBoolean(MANTER_CONECTADO,false);
		if(conectado)
			startActivity(new Intent(this,DashboardActivity.class));
    }

	//login temporario da aplicacao
    public void entrarOnClick(View v){
    	String usuarioInformado = usuario.getText().toString();
    	String senhaInformada = senha.getText().toString();

    	if("leitor".equals(usuarioInformado)  && "123".equals(senhaInformada)){

			//aplica as configurações(caso nao esteja aplicado) ao clicar em entrar ,
			//com o editor e possivel setar dados
			SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = preferencias.edit();
			editor.putBoolean(MANTER_CONECTADO,this.manterConectado.isChecked());
			editor.commit();

    		startActivity(new Intent(this,DashboardActivity.class));
    	}
    	else{
    		String mensagemErro = getString(R.string.erro_autenticao);
    		Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }
}