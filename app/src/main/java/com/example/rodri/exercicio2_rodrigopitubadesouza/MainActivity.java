package com.example.rodri.exercicio2_rodrigopitubadesouza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DAO.SeriesContract;
import DAO.SeriesDbHelper;

public class MainActivity extends AppCompatActivity {
    private Button btnInserir;
    private RecyclerView rclSeries;
    private DAO.SeriesDbHelper dbHelper;
    private LembreteAdapter adapter;

    private EditText edtNome;
    private EditText edtTemporada;
    private EditText edtEp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new SeriesDbHelper(getApplicationContext());
        adapter = new LembreteAdapter(getCursor());

        rclSeries = (RecyclerView) findViewById(R.id.rcl_series);
        rclSeries.setLayoutManager(new LinearLayoutManager(this));
        rclSeries.setAdapter(adapter);

        edtNome = (EditText) findViewById(R.id.edt_nome_serie);
        edtTemporada = (EditText) findViewById(R.id.edt_temporada);
        edtEp = (EditText) findViewById(R.id.edt_episodio);

        btnInserir = (Button) findViewById(R.id.btn_salvar);
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(SeriesContract.Lembrete.COLUMN_NAME_TITULO,String.valueOf(edtNome.getText()));
                values.put(SeriesContract.Lembrete.COLUMN_NAME_TEMPORADA,Integer.valueOf(String.valueOf(edtTemporada.getText())));
                values.put(SeriesContract.Lembrete.COLUMN_NAME_EP, Integer.valueOf(String.valueOf(edtEp.getText())));
                long id = db.insert(SeriesContract.Lembrete.TABLE_NAME,null, values);
                values.put(SeriesContract.Lembrete._ID, Long.valueOf(id));
                adapter.setCursor(getCursor());
            }
        });
        adapter.setOnSerieClickListener(new LembreteAdapter.OnSerieClickListener() {
            @Override
            public void onSerieClick(View view, int position) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String select = SeriesContract.Lembrete._ID+" = ?";
                String [] selectArgs = {Long.toString(adapter.getItemId(position))};
                db.delete(SeriesContract.Lembrete.TABLE_NAME, select, selectArgs);
                adapter.setCursor(getCursor());
                adapter.notifyItemRemoved(position);
            }
        });
        rclSeries.setAdapter(adapter);
    }

    private Cursor getCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] visao = {
                SeriesContract.Lembrete._ID,SeriesContract.Lembrete.COLUMN_NAME_ID, SeriesContract.Lembrete.COLUMN_NAME_TITULO,SeriesContract.Lembrete.COLUMN_NAME_TEMPORADA, SeriesContract.Lembrete.COLUMN_NAME_EP
        };
        String sort = SeriesContract.Lembrete.COLUMN_NAME_TITULO+ " ASC";
        return db.query(SeriesContract.Lembrete.TABLE_NAME,visao,null,null,null,null,sort);
    }
}
