package br.com.albovieira.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViagemListActivity extends ListActivity implements
		OnItemClickListener , OnClickListener, SimpleAdapter.ViewBinder{

	private List<Map<String, Object>> viagens;
	private AlertDialog alertDialog;
	private AlertDialog confirmaDialog;
	private int viagemSelecionada;
	final int VALOR_TOTAL = 0;
	final int VALOR_LIMITE = 1;
	final int VALOR_ATUAL = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
		int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};
		
		SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(), R.layout.lista_viagem, de, para);
		adapter.setViewBinder(this);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);

		this.alertDialog = criaAlertDialog();
		this.confirmaDialog = criaDialogConfirmacao();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		this.viagemSelecionada = position;
		this.alertDialog.show();
	}
	
	private List<Map<String, Object>> listarViagens() {
		viagens = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("imagem", R.drawable.negocios);
		item.put("destino", "São Paulo");
		item.put("data","02/02/2012 a 04/02/2012");
		item.put("total","Gasto total R$ 314,98");
		item.put("barraProgresso", new Double[]{1500.0, 2450.0, 314.98});
		viagens.add(item);
		
		item = new HashMap<String, Object>();
		item.put("imagem", R.drawable.lazer);
		item.put("destino", "Maceió");
		item.put("data","14/05/2012 a 22/05/2012");
		item.put("total", "Gasto total R$ 25834,67");
		item.put("barraProgresso", new Double[]{500.0, 450.0, 314.98});
		viagens.add(item);
		
		return viagens;
	}

	public boolean setViewValue(View view, Object data, String textRepresentation) {
		if (view.getId() == R.id.barraProgresso) {
			Double valores[] = (Double[]) data;
			ProgressBar progressBar = (ProgressBar) view;
			progressBar.setMax(valores[VALOR_TOTAL].intValue());
			progressBar.setSecondaryProgress(valores[VALOR_LIMITE].intValue());
			progressBar.setProgress(valores[VALOR_ATUAL].intValue());
			return true;
		}
		return false;
	}

	@Override
	public void onClick(DialogInterface dialog, int item) {
		switch (item){
			case 0:
				startActivity(new Intent(this,ViagemActivity.class));
				break;
			case 1:
				startActivity(new Intent(this,GastoActivity.class));
				break;
			case 2:
				startActivity(new Intent(this,GastoListActivity.class));
				break;
			case 3:
				this.confirmaDialog.show();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				this.viagens.remove(this.viagemSelecionada);
				getListView().invalidateViews();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				confirmaDialog.dismiss();
				break;
		}
	}

	private AlertDialog criaAlertDialog(){
		final CharSequence[] itens = {
				getString(R.string.editar),
				getString(R.string.novo_gasto),
				getString(R.string.gastos_realizados),
				getString(R.string.remover),
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.opcoes);
		builder.setItems(itens,this);

		return builder.create();
	}

	private AlertDialog criaDialogConfirmacao(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmacao_exclusao_viagem);
		builder.setPositiveButton(getString(R.string.sim), this);
		builder.setNegativeButton(getString(R.string.nao),this);

		return builder.create();
	}
}
