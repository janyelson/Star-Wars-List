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

/**
 * Created by MEU PC on 05/07/2017.
 */

public class Description extends AppCompatActivity {

    private static String url;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private ArrayList<Item> values_films, values_species, values_vehicles, values_starship;
    public static JSONObject jsonObject = null;

    private TextView textView_name, textView_height, textView_mass, textView_hair_color, textView_skin_color,
            textView_eye_color, textView_birth_year, textView_homeworld;

    private SimpleListAdapter simpleListAdapter_films, simpleListAdapter_species, simpleListAdapter_vehicles, simpleListAdapter_starship;
    private CustomListView listView_films, listView_species, listView_vehicles, listView_starship;

    private String homeworldUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        url = getIntent().getExtras().getString("url");
        starWarsAPI = new StarWarsAPI();
        ctx = this;
        starWarsAPI.getPeople(url,ctx);

        values_films = new ArrayList<Item>();
        values_species = new ArrayList<Item>();
        values_vehicles = new ArrayList<Item>();
        values_starship = new ArrayList<Item>();

        simpleListAdapter_films = new SimpleListAdapter(ctx, values_films);
        simpleListAdapter_species = new SimpleListAdapter(ctx, values_species);
        simpleListAdapter_vehicles = new SimpleListAdapter(ctx, values_vehicles);
        simpleListAdapter_starship = new SimpleListAdapter(ctx, values_starship);

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_height = (TextView) findViewById(R.id.textView_height_value);
        textView_mass = (TextView) findViewById(R.id.textView_mass_value);
        textView_hair_color = (TextView) findViewById(R.id.textView_hair_color_value);
        textView_skin_color = (TextView) findViewById(R.id.textView_skin_color_value);
        textView_eye_color = (TextView) findViewById(R.id.textView_eye_color_value);
        textView_birth_year = (TextView) findViewById(R.id.textView_birth_year_value);

        textView_homeworld = (TextView) findViewById(R.id.textView_homeworld_value);
        textView_homeworld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPage("planet", 0);
            }
        });

        listView_films = (CustomListView) findViewById(R.id.list_films);
        listView_species = (CustomListView) findViewById(R.id.list_species);
        listView_vehicles = (CustomListView) findViewById(R.id.list_vehicles);
        listView_starship = (CustomListView) findViewById(R.id.list_starships);

        listView_films.setAdapter(simpleListAdapter_films);
        listView_species.setAdapter(simpleListAdapter_species);
        listView_vehicles.setAdapter(simpleListAdapter_vehicles);
        listView_starship.setAdapter(simpleListAdapter_starship);

        listView_films.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("film", position);
            }
        });

        listView_species.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("specie", position);
            }
        });

        listView_vehicles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("vehicle", position);
            }
        });

        listView_starship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("starship", position);
            }
        });

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        new WaitAsync().execute();
    }

    private void toPage(String category, int position) {
        Intent intent;
        switch(category) {
            case "planet":
                if(homeworldUrl != null) {

                    intent = new Intent(this, DescriptionPlanet.class);
                    intent.putExtra("url", homeworldUrl);
                    startActivity(intent);
                }
                break;
            case "film":
                intent = new Intent(this, DescriptionFilm.class);
                intent.putExtra("url", simpleListAdapter_films.getItem(position).url);
                startActivity(intent);
                break;
            case "specie":
                intent = new Intent(this, DescriptionSpecie.class);
                intent.putExtra("url", simpleListAdapter_species.getItem(position).url);
                startActivity(intent);
                break;
            case "vehicle":
                intent = new Intent(this, DescriptionVehicle.class);
                intent.putExtra("url", simpleListAdapter_vehicles.getItem(position).url);
                startActivity(intent);
                break;
            case "starship":
                intent = new Intent(this, DescriptionStarship.class);
                intent.putExtra("url", simpleListAdapter_starship.getItem(position).url);
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
                textView_height.setText(jsonObject.getString("height"));
                textView_mass.setText(jsonObject.getString("mass"));
                textView_hair_color.setText(jsonObject.getString("hair_color"));
                textView_skin_color.setText(jsonObject.getString("skin_color"));
                textView_eye_color.setText(jsonObject.getString("eye_color"));
                textView_birth_year.setText(jsonObject.getString("birth_year"));

                homeworldUrl = jsonObject.getString("homeworld");
                starWarsAPI.getName(homeworldUrl, ctx, textView_homeworld, "name");

                JSONArray jsonArray = jsonObject.getJSONArray("films");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_films, "title");
                }

                jsonArray = jsonObject.getJSONArray("species");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_species, "name");

                }

                jsonArray = jsonObject.getJSONArray("vehicles");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_vehicles, "name");
                }

                jsonArray = jsonObject.getJSONArray("starships");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_starship, "name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObject = null;
            Log.v("Complete", "Description is loaded!");
        }
    }
}
