package com.unisc.ex_aula7;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvTemperatura, tvUmidade, tvPontoOrvalho, tvPressao;
    private ListView listView;
    String de[] =  {"temp","umi","orv","pres"};
    int para [] = {R.id.tvTemp,R.id.tvUmi,R.id.tvPontoO,R.id.tvPres};
    List<Map<String,String>> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemperatura = findViewById(R.id.tvTemperatura);
        tvUmidade = findViewById(R.id.tvUmidade);
        tvPontoOrvalho = findViewById(R.id.tvPontoOrvalho);
        tvPressao = findViewById(R.id.tvPressao);

        listView = findViewById(R.id.listaView);

    }

    public void cliqueBuscar(View view) {
        lista = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ghelfer.net/la/weather.json", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data = new String(responseBody);
                //Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                try {
                    carregaDados(data);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void carregaDados(String data) throws JSONException {
        double sumTemp = 0;
        double sumUmi = 0;
        double sumorv = 0;
        double sumpres = 0;
        JSONObject res = new JSONObject(data);
        JSONArray array = res.getJSONArray("weather");
        for(int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            String temp = json.get("temperature").toString();
            sumTemp += Double.parseDouble(temp);
            String umi = json.get("humidity").toString();
            sumUmi += Double.parseDouble(umi);
            String orv = json.get("dewpoint").toString();
            sumorv += Double.parseDouble(orv);
            String pres = json.get("pressure").toString();
            sumpres += Double.parseDouble(pres);

            Map<String,String> mapa = new HashMap<>();
            mapa.put("temp",temp);
            mapa.put("umi",umi);
            mapa.put("orv",orv);
            mapa.put("pres",pres);
            lista.add(mapa);
        }
        Double tam = Double.parseDouble(String.valueOf(array.length()));
        tvTemperatura.setText(String.valueOf(sumTemp/tam));
        tvUmidade.setText(String.valueOf(sumUmi/tam));
        tvPontoOrvalho.setText(String.valueOf(sumorv/tam));
        tvPressao.setText(String.valueOf(sumpres/tam));

       // SimpleAdapter adpter = new SimpleAdapter(this, lista,R.layout.visual,de,para);
        //listView.setAdapter(adapter);
    }
}
