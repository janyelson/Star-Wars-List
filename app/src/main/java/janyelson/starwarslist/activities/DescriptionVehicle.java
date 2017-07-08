package janyelson.starwarslist.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import janyelson.starwarslist.R;
import janyelson.starwarslist.adapters.SimpleListAdapter;
import janyelson.starwarslist.utils.CustomListView;
import janyelson.starwarslist.utils.Item;
import janyelson.starwarslist.utils.StarWarsAPI;

public class DescriptionVehicle extends AppCompatActivity {

    private static String url;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private ArrayList<Item> values_films, values_pilots;
    public static JSONObject jsonObject = null;

    private TextView textView_name, textView_model, textView_manufacturer, textView_cost_in_credits, textView_length,
            textView_max_atmosphering_speed, textView_crew, textView_passengers, textView_cargo_capacity, textView_consumables,
            textView_vehicle_class;

    private ProgressDialog dialog;

    private SimpleListAdapter simpleListAdapter_films, simpleListAdapter_pilots;
    private CustomListView listView_films, listView_pilots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        beginDialog();
        url = getIntent().getExtras().getString("url");
        starWarsAPI = new StarWarsAPI();
        ctx = this;
        starWarsAPI.getVehicle(url,ctx);

        values_films = new ArrayList<Item>();
        values_pilots = new ArrayList<Item>();

        simpleListAdapter_films = new SimpleListAdapter(ctx, values_films);
        simpleListAdapter_pilots = new SimpleListAdapter(ctx, values_pilots);

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_model= (TextView) findViewById(R.id.textView_model_value);
        textView_manufacturer = (TextView) findViewById(R.id.textView_manufacturer_value);
        textView_cost_in_credits = (TextView) findViewById(R.id.textView_cost_in_credits_value);
        textView_length = (TextView) findViewById(R.id.textView_length_value);
        textView_max_atmosphering_speed = (TextView) findViewById(R.id.textView_max_atmosphering_speed_value);
        textView_crew= (TextView) findViewById(R.id.textView_crew_value);
        textView_passengers = (TextView) findViewById(R.id.textView_passengers_value);
        textView_cargo_capacity = (TextView) findViewById(R.id.textView_cargo_capacity_value);
        textView_consumables = (TextView) findViewById(R.id.textView_consumables_value);
        textView_vehicle_class = (TextView) findViewById(R.id.textView_vehicle_class_value);

        listView_films = (CustomListView) findViewById(R.id.list_films);
        listView_pilots = (CustomListView) findViewById(R.id.list_pilots);

        listView_films.setAdapter(simpleListAdapter_films);
        listView_pilots.setAdapter(simpleListAdapter_pilots);

        listView_films.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("film", position);
            }
        });

        listView_pilots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("people", position);
            }
        });

        dialog.show();

        new WaitAsync().execute();
    }

    private void toPage(String category, int position) {
        Intent intent;
        switch(category) {
            case "people":
                intent = new Intent(this, Description.class);
                intent.putExtra("url", simpleListAdapter_pilots.getItem(position).url);
                startActivity(intent);
                break;
            case "film":
                intent = new Intent(this, DescriptionFilm.class);
                intent.putExtra("url", simpleListAdapter_films.getItem(position).url);
                startActivity(intent);
                break;
        }
    }

    private void beginDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    private class WaitAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(jsonObject == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                textView_name.setText(jsonObject.getString("name"));
                textView_model.setText(jsonObject.getString("model"));
                textView_manufacturer.setText(jsonObject.getString("manufacturer"));
                textView_cost_in_credits.setText(jsonObject.getString("cost_in_credits"));
                textView_length.setText(jsonObject.getString("length"));
                textView_max_atmosphering_speed.setText(jsonObject.getString("max_atmosphering_speed"));
                textView_crew.setText(jsonObject.getString("crew"));
                textView_passengers.setText(jsonObject.getString("passengers"));
                textView_cargo_capacity.setText(jsonObject.getString("cargo_capacity"));
                textView_consumables.setText(jsonObject.getString("consumables"));
                textView_vehicle_class.setText(jsonObject.getString("vehicle_class"));

                JSONArray jsonArray = jsonObject.getJSONArray("films");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_films, "title");
                }

                jsonArray = jsonObject.getJSONArray("pilots");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_pilots, "name");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                dialog.dismiss();
            }
            jsonObject = null;
            Log.v("Complete", "Description is loaded!");
        }
    }
}
