package janyelson.starwarslist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class DescriptionPlanet extends AppCompatActivity {

    private static String url;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private ArrayList<Item> values_films, values_residents;
    public static JSONObject jsonObject = null;

    private TextView textView_name, textView_rotation_period, textView_orbital_period, textView_diameter, textView_climate,
            textView_gravity, textView_terrain, textView_surface_water, textView_population;

    private SimpleListAdapter simpleListAdapter_films, simpleListAdapter_residents;
    private CustomListView listView_films, listView_residents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_planet);
        url = getIntent().getExtras().getString("url");
        starWarsAPI = new StarWarsAPI();
        ctx = this;
        starWarsAPI.getPlanet(url,ctx);

        values_films = new ArrayList<Item>();
        values_residents = new ArrayList<Item>();

        simpleListAdapter_films = new SimpleListAdapter(ctx, values_films);
        simpleListAdapter_residents = new SimpleListAdapter(ctx, values_residents);

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_rotation_period = (TextView) findViewById(R.id.textView_rotation_period_value);
        textView_orbital_period = (TextView) findViewById(R.id.textView_orbital_period_value);
        textView_diameter = (TextView) findViewById(R.id.textView_diameter_value);
        textView_climate = (TextView) findViewById(R.id.textView_climate_value);
        textView_gravity = (TextView) findViewById(R.id.textView_gravity_value);
        textView_terrain = (TextView) findViewById(R.id.textView_terrain_value);
        textView_surface_water = (TextView) findViewById(R.id.textView_surface_water_value);
        textView_population = (TextView) findViewById(R.id.textView_population_value);

        listView_films = (CustomListView) findViewById(R.id.list_films);
        listView_residents = (CustomListView) findViewById(R.id.list_residents);

        listView_films.setAdapter(simpleListAdapter_films);
        listView_residents.setAdapter(simpleListAdapter_residents);

        listView_films.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("film", position);
            }
        });

        listView_residents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("people", position);
            }
        });

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        new WaitAsync().execute();
    }

    private void toPage(String category, int position) {
        Intent intent;
        switch(category) {
            case "people":
                intent = new Intent(this, Description.class);
                intent.putExtra("url", simpleListAdapter_residents.getItem(position).url);
                startActivity(intent);
                break;
            case "film":
                intent = new Intent(this, DescriptionFilm.class);
                intent.putExtra("url", simpleListAdapter_films.getItem(position).url);
                startActivity(intent);
                break;
        }
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
                textView_rotation_period.setText(jsonObject.getString("rotation_period"));
                textView_orbital_period.setText(jsonObject.getString("orbital_period"));
                textView_diameter.setText(jsonObject.getString("diameter"));
                textView_climate.setText(jsonObject.getString("climate"));
                textView_gravity.setText(jsonObject.getString("gravity"));
                textView_terrain.setText(jsonObject.getString("terrain"));
                textView_surface_water.setText(jsonObject.getString("surface_water"));
                textView_population.setText(jsonObject.getString("population"));

                JSONArray jsonArray = jsonObject.getJSONArray("films");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_films, "title");
                }

                jsonArray = jsonObject.getJSONArray("residents");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_residents, "name");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObject = null;
            Log.v("Complete", "Description is loaded!");
        }
    }
}
