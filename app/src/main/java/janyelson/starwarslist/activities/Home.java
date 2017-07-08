package janyelson.starwarslist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import janyelson.starwarslist.R;
import janyelson.starwarslist.adapters.MainListAdapter;
import janyelson.starwarslist.utils.Item;
import janyelson.starwarslist.utils.StarWarsAPI;

public class Home extends AppCompatActivity {

    private ListView listView;
    private MainListAdapter mainListAdapter;
    private static String category = "people";
    private ArrayList<Item> arrayList;
    private StarWarsAPI starWarsAPI;
    private Context ctx;
    private Button buttonBack, buttonNext;
    private TextView textView_pags;
    private static int pag = 1;
    public static int pagMax = 1;
    private static int pagMin = 1;

    //protected Object mActionMode;
    private int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = this;
        starWarsAPI = new StarWarsAPI();
        arrayList = new ArrayList<Item>();
        listView = (ListView) findViewById(R.id.starwarsList);
        mainListAdapter = new MainListAdapter(this, arrayList);

        listView.setAdapter(mainListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                if(selectedItem == -1) {
                    return;
                }

                openDescription(position);

            }
        });
        new WaitAsync().execute();
        buttonBack = (Button) findViewById(R.id.back_button);
        buttonNext = (Button) findViewById(R.id.next_button);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pag != pagMin) {
                    back();
                }
                else {
                    Log.e("Button Back", "Nao da mais para voltar");
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pag != pagMax) {
                    next();
                }
                else {
                    Log.e("Button Back", "Nao da mais para passar");
                }
            }
        });

        textView_pags = (TextView) findViewById(R.id.textView_pags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDescription(int position) {
        try {
            Intent intent = new Intent(this, Description.class);
            intent.putExtra("url", mainListAdapter.getItem(position).url);
            startActivity(intent);
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void back() {
        pag--;
        //textView_pags.setText(pag + "/" + pagMax);
        new WaitAsync().execute();
    }

    private void next() {
        pag++;
        //textView_pags.setText(pag + "/" + pagMax);
        new WaitAsync().execute();
    }

    private class WaitAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            starWarsAPI.getAll(category, pag, ctx, mainListAdapter, textView_pags);
            Log.v("Complete", "List is loaded!");
        }
    }
}
