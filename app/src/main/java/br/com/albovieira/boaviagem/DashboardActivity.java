package br.com.albovieira.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DashboardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
	}

	//seta as opcoes de menu da dashboard
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureID, MenuItem item){
		//fecha a aplicacao
		finish();
		return true;
	}

	//selecao das opcoes do menu principal
	public void selecionarOpcao(View view) {
		switch (view.getId()) {
			case R.id.nova_viagem:
				startActivity(new Intent(this, ViagemActivity.class));
				break;
			case R.id.novo_gasto:
				startActivity(new Intent(this, GastoActivity.class));
				break;
			case R.id.minhas_viagens:
				startActivity(new Intent(this, ViagemListActivity.class));
				break;
			case R.id.configuracoes:
				startActivity(new Intent(this, ConfiguracoesActivity.class));
				break;
		}
	}
}

