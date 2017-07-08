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

public class DescriptionFilm extends AppCompatActivity {

    private static String url;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private ArrayList<Item> values_characters, values_species, values_vehicles, values_starship, values_planets;
    public static JSONObject jsonObject = null;

    private ProgressDialog dialog;

    private TextView textView_title, textView_episode, textView_opening_crawl, textView_director, textView_producer,
            textView_release_date;

    private SimpleListAdapter simpleListAdapter_characters, simpleListAdapter_species, simpleListAdapter_vehicles, simpleListAdapter_starship,
            simpleListAdapter_planets;
    private CustomListView listView_characters, listView_species, listView_vehicles, listView_starship, listView_planets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_film);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url = getIntent().getExtras().getString("url");
        starWarsAPI = new StarWarsAPI();
        ctx = this;
        beginDialog();
        starWarsAPI.getFilm(url,ctx);

        values_characters = new ArrayList<Item>();
        values_species = new ArrayList<Item>();
        values_vehicles = new ArrayList<Item>();
        values_starship = new ArrayList<Item>();
        values_planets = new ArrayList<Item>();

        simpleListAdapter_characters = new SimpleListAdapter(ctx, values_characters);
        simpleListAdapter_species = new SimpleListAdapter(ctx, values_species);
        simpleListAdapter_vehicles = new SimpleListAdapter(ctx, values_vehicles);
        simpleListAdapter_starship = new SimpleListAdapter(ctx, values_starship);
        simpleListAdapter_planets = new SimpleListAdapter(ctx, values_planets);

        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_episode = (TextView) findViewById(R.id.textView_episode_value);
        textView_opening_crawl = (TextView) findViewById(R.id.textView_opening_crawl_value);
        textView_director = (TextView) findViewById(R.id.textView_director_value);
        textView_producer = (TextView) findViewById(R.id.textView_producer_value);
        textView_release_date = (TextView) findViewById(R.id.textView_release_date_value);

        listView_characters = (CustomListView) findViewById(R.id.list_characters);
        listView_species = (CustomListView) findViewById(R.id.list_species);
        listView_vehicles = (CustomListView) findViewById(R.id.list_vehicles);
        listView_starship = (CustomListView) findViewById(R.id.list_starships);
        listView_planets = (CustomListView) findViewById(R.id.list_planets);

        listView_characters.setAdapter(simpleListAdapter_characters);
        listView_species.setAdapter(simpleListAdapter_species);
        listView_vehicles.setAdapter(simpleListAdapter_vehicles);
        listView_starship.setAdapter(simpleListAdapter_starship);
        listView_planets.setAdapter(simpleListAdapter_planets);

        listView_characters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("people", position);
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


        dialog.show();

        new WaitAsync().execute();
    }

    private void toPage(String category, int position) {
        Intent intent;
        switch(category) {
            case "people":
                intent = new Intent(this, Description.class);
                intent.putExtra("url", simpleListAdapter_characters.getItem(position).url);
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
                textView_title.setText(jsonObject.getString("title"));
                String episode = "" + jsonObject.getInt("episode_id");
                textView_episode.setText(episode);
                textView_opening_crawl.setText(jsonObject.getString("opening_crawl"));
                textView_director.setText(jsonObject.getString("director"));
                textView_producer.setText(jsonObject.getString("producer"));
                textView_release_date.setText(jsonObject.getString("release_date"));

                JSONArray jsonArray = jsonObject.getJSONArray("characters");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_characters, "name");
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

                jsonArray = jsonObject.getJSONArray("planets");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_planets, "name");
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
