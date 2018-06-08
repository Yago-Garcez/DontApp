package com.example.yagog.meteorologia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrevisaoActivity extends AppCompatActivity {

    private ImageView conditionImageView;
    private TextView dayTextView, lowTextView, hightTextView, humidityTextView, locationTextView;
    private ListView weatherListView;
    private List<Previsao> previsoes;
    private ArrayAdapter<Previsao> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previsao);

        Intent it = getIntent();
        String nomeCidade = it.getStringExtra("CIDADE");

        locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(getString(R.string.previsao_para, nomeCidade));
        //locationTextView.setText(nomeCidade);
        conditionImageView = findViewById(R.id.conditionImageView);
        dayTextView = findViewById(R.id.dayTextView);
        lowTextView = findViewById(R.id.lowTextView);
        hightTextView = findViewById(R.id.highTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        weatherListView = findViewById(R.id.weatherListView);
        previsoes = new LinkedList<>();
        adapter = new ArrayAdapter<Previsao>(this, -1, previsoes){
            class ViewHolder{
                ImageView conditionImageView;
                TextView dayTextView, lowTextView, highTextView, humidtyTextView;
            }
            private Map<String, Bitmap> icones = new HashMap<>();

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                ViewHolder viewHolder;
                ImageView conditionImageView;
                TextView dayTextView, lowTextView, hightTextView, humidityTextView;

                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater) PrevisaoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item, parent, false);
                    viewHolder = new ViewHolder();
                    conditionImageView = convertView.findViewById(R.id.conditionImageView);
                    dayTextView = convertView.findViewById(R.id.dayTextView);
                    lowTextView = convertView.findViewById(R.id.lowTextView);
                    hightTextView = convertView.findViewById(R.id.highTextView);
                    humidityTextView = convertView.findViewById(R.id.humidityTextView);
                    viewHolder.conditionImageView = conditionImageView;
                    viewHolder.dayTextView = dayTextView;
                    viewHolder.lowTextView = lowTextView;
                    viewHolder.highTextView = hightTextView;
                    viewHolder.humidtyTextView = humidityTextView;
                    convertView.setTag(viewHolder);
                }
                else{
                    viewHolder = (ViewHolder) convertView.getTag();
                    conditionImageView = viewHolder.conditionImageView;
                    dayTextView = viewHolder.dayTextView;
                    lowTextView = viewHolder.lowTextView;
                    hightTextView = viewHolder.highTextView;
                    humidityTextView = viewHolder.humidtyTextView;
                }

                Previsao p = previsoes.get(position);

                if(icones.containsKey(p.getIcon())){
                    Bitmap b = icones.get(p.getIcon());
                    conditionImageView.setImageBitmap(b);
                }
                else{
                    new BaixaImagem(p, icones, conditionImageView).execute(getString(R.string.images_url, p.getIcon()));
                }

                String diaDaSemana = DataUtils.obtemDiaSemana(p.getDt());
                dayTextView.setText(getString(R.string.day_description, diaDaSemana, p.getDescription()));
                lowTextView.setText(getString(R.string.low_temp, Double.toString(p.getMin())));
                hightTextView.setText(getString(R.string.high_temp, Double.toString(p.getMax())));
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                double v = p.getHumidity() / 100d;
                humidityTextView.setText(getString(R.string.humidity, percentFormat.format(v)));
                return convertView;
            }
        };


        StringBuilder sb = new StringBuilder("");
        sb.append(getString(R.string.web_service_url));
        sb.append(nomeCidade);
        sb.append("&appid=");
        sb.append(getString(R.string.api_key));
        sb.append("&units=metric&lang=pt&cnt=16");
        String url = sb.toString();
        new ConsomeWSPrevisaoDoTempo().execute(url);

        weatherListView.setAdapter(adapter);
    }

/*    @Override
    protected void onStart() {
        super.onStart();
        String nomeCidade = locationTextView.getText().toString();
        StringBuilder sb = new StringBuilder("");
        sb.append(getString(R.string.web_service_url));
        sb.append(nomeCidade);
        sb.append("&appid=");
        sb.append(getString(R.string.api_key));
        sb.append("&units=metric&lang=pt&cnt=16");
        String url = sb.toString();
        new ConsomeWSPrevisaoDoTempo().execute(url);
    }*/

    private class BaixaImagem extends AsyncTask<String, Void, Bitmap>{
        private ImageView conditionImageView;
        private Map<String, Bitmap> icones;
        private Previsao p;
        public BaixaImagem(Previsao p, Map<String, Bitmap> icones, ImageView conditionImageView){
            this.conditionImageView = conditionImageView;
            this.icones = icones;
            this.p = p;
        }
        @Override
        protected Bitmap doInBackground(String... url){
            try{
                URL u = new URL(url[0]);
                HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                InputStream is = connection.getInputStream();
                Bitmap icone = BitmapFactory.decodeStream(is);
                icones.put(p.getIcon(), icone);
                return icone;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            conditionImageView.setImageBitmap(bitmap);
        }
    }

    private class ConsomeWSPrevisaoDoTempo extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... url) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url[0]).build();
            Response response = null;

            try{
                response = client.newCall(request).execute();
                String resultado = response.body().string();
                return resultado;
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            previsoes.clear();
            try{
                JSONObject previsao = new JSONObject(resultado);
                JSONArray list = previsao.getJSONArray("list");
                for(int i = 0; i < list.length(); i++){
                    JSONObject dia = list.getJSONObject(i);
                    long dt = dia.getLong("dt");
                    JSONObject temp = dia.getJSONObject("temp");
                    double min = temp.getDouble("min");
                    double max = temp.getDouble("max");
                    int humidity = dia.getInt("humidity");
                    JSONArray weather = dia.getJSONArray("weather");
                    JSONObject detalhes = weather.getJSONObject(0);
                    String description = detalhes.getString("description");
                    String icon = detalhes.getString("icon");
                    Previsao p = new Previsao(dt, min, max, humidity, description, icon);
                    previsoes.add(p);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
