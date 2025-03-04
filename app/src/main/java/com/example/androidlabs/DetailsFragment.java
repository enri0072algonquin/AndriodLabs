package com.example.androidlabs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    private TextView nameTextView, heightTextView, massTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Find TextViews
        nameTextView = view.findViewById(R.id.nameTextView);
        heightTextView = view.findViewById(R.id.heightTextView);
        massTextView = view.findViewById(R.id.massTextView);

        // Get arguments and update UI
        if (getArguments() != null) {
            String name = getArguments().getString("name", "Unknown");
            String height = getArguments().getString("height", "Unknown");
            String mass = getArguments().getString("mass", "Unknown");

            nameTextView.setText("Name: " + name);
            heightTextView.setText("Height: " + height);
            massTextView.setText("Mass: " + mass);
        }

        return view;
    }
}
