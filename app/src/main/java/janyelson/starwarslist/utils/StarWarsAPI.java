package janyelson.starwarslist.utils;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import janyelson.starwarslist.activities.Description;
import janyelson.starwarslist.activities.DescriptionFilm;
import janyelson.starwarslist.activities.DescriptionPlanet;
import janyelson.starwarslist.activities.DescriptionSpecie;
import janyelson.starwarslist.activities.DescriptionStarship;
import janyelson.starwarslist.activities.DescriptionVehicle;
import janyelson.starwarslist.activities.Home;
import janyelson.starwarslist.adapters.MainListAdapter;
import janyelson.starwarslist.adapters.SimpleListAdapter;

/**
 * Created by MEU PC on 04/07/2017.
 */

public class StarWarsAPI {

    public static String urlAPI = "http://swapi.co/api/";
    private RequestQueue mRequestQueue;

    public StarWarsAPI() {}

    public void getAll(final String category, final int pag, Context ctx, final MainListAdapter adapter, final TextView num_pags) {
        adapter.clear();
        adapter.notifyDataSetChanged();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPI + category + "/?page=" + pag, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Home.pagMax = (int) Math.round(response.getInt("count")/10.0);
                    num_pags.setText(pag + "/" + Home.pagMax);
                    JSONArray jsonArray = response.getJSONArray("results");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String camp = "name";
                        if (category == "films") {

                            camp = "title";
                        }
                        Item item = new Item(jsonObject.getString(camp), jsonObject.getString("url"));
                        adapter.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

    }

    public void getPeople(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Description.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getPlanet(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DescriptionPlanet.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getFilm(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DescriptionFilm.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getSpecie(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DescriptionSpecie.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getStarship(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DescriptionStarship.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getVehicle(String url, Context ctx) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DescriptionVehicle.jsonObject = response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getName(final String url, Context ctx, final SimpleListAdapter adapter, final String camp) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    adapter.add(new Item(response.getString(camp), url));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }

    public void getName(String url, Context ctx, final TextView textView, final String camp) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textView.setText(response.getString(camp));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(ctx);
        mRequestQueue.add(jsonObjectRequest);

        return;
    }
}
