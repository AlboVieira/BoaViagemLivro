package br.com.albovieira.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	private DatabaseHelper helper;
	private Double valorLimite;
	private SimpleDateFormat dateFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new DatabaseHelper(this);
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
		String valor = preferencias.getString("valor_limite","-1");
		valorLimite = Double.valueOf(valor);

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

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor =
				db.rawQuery(
						"SELECT _id,tipo_viagem,destino," +
								"data_chegada,data_saida,orcamento" +
								" FROM viagem", null);
		cursor.moveToFirst();

		//percorre cada item da viagem e atribui a uma variavem
		viagens = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<cursor.getCount();i++){

			Map<String, Object> item = new HashMap<String, Object>();

			String id = cursor.getString(0);
			int tipoViagem = cursor.getInt(1);
			String destino = cursor.getString(2);
			long dataChegada = cursor.getLong(3);
			long dataSaida = cursor.getLong(4);
			double orcamento = cursor.getDouble(5);

			item.put("id", id);
			if (tipoViagem == Constantes.VIAGEM_LAZER) {
				item.put("imagem", R.drawable.lazer);
			} else {
				item.put("imagem", R.drawable.negocios);
			}
			item.put("destino", destino);
			Date dataChegadaDate = new Date(dataChegada);
			Date dataSaidaDate = new Date(dataSaida);

			String periodo = dateFormat.format(dataChegadaDate) + "a" + dateFormat.format(dataSaidaDate);

			double totalGasto = calcularTotalGasto(db, id);
			item.put("total", "Gasto total R$ " + totalGasto);
			double alerta = orcamento * valorLimite / 100;
			Double [] valores =
					new Double[] { orcamento, alerta, totalGasto };
			item.put("barraProgresso", valores);
			viagens.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return viagens;
	}
	public double calcularTotalGasto(SQLiteDatabase db, String id){
		Cursor cursor = db.rawQuery(
				"SELECT SUM(valor) FROM gasto" +
						"WHERE viagem_id=?"
		,new String[]{id});
		cursor.moveToFirst();
		double total = cursor.getDouble(0);
		cursor.close();

		return total;
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
