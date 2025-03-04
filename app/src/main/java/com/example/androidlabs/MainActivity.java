
package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> characterNames = new ArrayList<>();
    private ArrayList<HashMap<String, String>> characterDetails = new ArrayList<>();
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.ListViewFull);
        adapter = new CharacterAdapter(this, characterNames);
        listView.setAdapter(adapter);

        new FetchStarWarsData().execute("https://swapi.dev/api/people/?format=json");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> character = characterDetails.get(position);

                View fragmentContainer = findViewById(R.id.detailContainer);
                if (fragmentContainer != null) {
                    // Tablet → Load DetailsFragment inside MainActivity
                    showDetailsFragment(character);
                } else {
                    // Phone → Open EmptyActivity
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtra("name", character.get("name"));
                    intent.putExtra("height", character.get("height"));
                    intent.putExtra("mass", character.get("mass"));
                    startActivity(intent);
                }
            }
        });
    }

    // Load DetailsFragment into MainActivity (Tablet View)
    private void showDetailsFragment(HashMap<String, String> character) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("name", character.get("name"));
        args.putString("height", character.get("height"));
        args.putString("mass", character.get("mass"));
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.detailContainer, fragment);
        transaction.commit();
    }

    // AsyncTask to fetch Star Wars data from API
    private class FetchStarWarsData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject character = results.getJSONObject(i);
                    String name = character.getString("name");
                    String height = character.getString("height") + " cm";
                    String mass = character.getString("mass") + " kg";

                    characterNames.add(name);

                    HashMap<String, String> details = new HashMap<>();
                    details.put("name", name);
                    details.put("height", height);
                    details.put("mass", mass);
                    characterDetails.add(details);
                }

                adapter.notifyDataSetChanged(); // Refresh ListView

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
