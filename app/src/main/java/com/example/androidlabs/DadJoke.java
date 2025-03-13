package com.example.androidlabs;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DadJoke extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dad_joke);

        TextView jokeTextView = findViewById(R.id.joke_text);
        jokeTextView.setText("Why don't skeletons fight each other? Because they don't have the guts!"); // 🃏 Your dad joke
    }
}
