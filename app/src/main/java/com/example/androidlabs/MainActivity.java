package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoList;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv2 = findViewById(R.id.LV2);
        EditText etTodoText = findViewById(R.id.etTodoText);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Initialize list and adapter
        todoList = new ArrayList<>();
        adapter = new TodoAdapter(this, todoList);
        lv2.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = etTodoText.getText().toString().trim();
                if (!todoText.isEmpty()) {
                    todoList.add(new TodoItem(todoText, false)); // New todo item (not urgent)
                    adapter.notifyDataSetChanged();
                    etTodoText.setText(""); // Clear input field
                }
            }
        });
    }
}
