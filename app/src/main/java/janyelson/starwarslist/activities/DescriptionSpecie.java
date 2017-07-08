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

public class DescriptionSpecie extends AppCompatActivity {

    private static String url;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private ArrayList<Item> values_films, values_people;
    public static JSONObject jsonObject = null;

    private TextView textView_name, textView_classification, textView_designation, textView_average_height, textView_skin_colors,
            textView_hair_colors, textView_eye_colors, textView_average_lifespan, textView_homeworld, textView_language;

    private SimpleListAdapter simpleListAdapter_films, simpleListAdapter_people;
    private CustomListView listView_films, listView_people;

    private String homeworldUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_specie);
        url = getIntent().getExtras().getString("url");
        starWarsAPI = new StarWarsAPI();
        ctx = this;
        starWarsAPI.getSpecie(url,ctx);

        values_films = new ArrayList<Item>();
        values_people = new ArrayList<Item>();

        simpleListAdapter_films = new SimpleListAdapter(ctx, values_films);
        simpleListAdapter_people = new SimpleListAdapter(ctx, values_people);

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_classification = (TextView) findViewById(R.id.textView_classification_value);
        textView_designation = (TextView) findViewById(R.id.textView_designation_value);
        textView_average_height = (TextView) findViewById(R.id.textView_average_height_value);
        textView_skin_colors = (TextView) findViewById(R.id.textView_skin_colors_value);
        textView_hair_colors = (TextView) findViewById(R.id.textView_hair_colors_value);
        textView_eye_colors = (TextView) findViewById(R.id.textView_eye_colors_value);
        textView_average_lifespan = (TextView) findViewById(R.id.textView_average_lifespan_value);
        textView_homeworld = (TextView) findViewById(R.id.textView_homeworld_value);

        textView_homeworld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPage("planet", 0);
            }
        });

        textView_language = (TextView) findViewById(R.id.textView_language_value);

        listView_films = (CustomListView) findViewById(R.id.list_films);
        listView_people = (CustomListView) findViewById(R.id.list_people);

        listView_films.setAdapter(simpleListAdapter_films);
        listView_people.setAdapter(simpleListAdapter_people);

        listView_films.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPage("film", position);
            }
        });

        listView_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            case "planet":
                if(homeworldUrl != null) {

                    intent = new Intent(this, DescriptionPlanet.class);
                    intent.putExtra("url", homeworldUrl);
                    startActivity(intent);
                }
                break;
            case "people":
                intent = new Intent(this, Description.class);
                intent.putExtra("url", simpleListAdapter_people.getItem(position).url);
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
                textView_classification.setText(jsonObject.getString("classification"));
                textView_designation.setText(jsonObject.getString("designation"));
                textView_average_height.setText(jsonObject.getString("average_height"));
                textView_skin_colors.setText(jsonObject.getString("skin_colors"));
                textView_hair_colors.setText(jsonObject.getString("hair_colors"));
                textView_eye_colors.setText(jsonObject.getString("eye_colors"));
                textView_average_lifespan.setText(jsonObject.getString("average_lifespan"));

                homeworldUrl = jsonObject.getString("homeworld");
                starWarsAPI.getName(homeworldUrl, ctx, textView_homeworld, "name");

                textView_language.setText(jsonObject.getString("language"));

                JSONArray jsonArray = jsonObject.getJSONArray("films");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_films, "title");
                }

                jsonArray = jsonObject.getJSONArray("people");
                for(int i = 0; i < jsonArray.length(); i++) {
                    starWarsAPI.getName(jsonArray.getString(i), ctx, simpleListAdapter_people, "name");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObject = null;
            Log.v("Complete", "Description is loaded!");
        }
    }
}
