package com.example.acme_explorer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.acme_explorer.MainActivity;
import com.example.acme_explorer.R;
import com.example.acme_explorer.adapters.MenuAdapter;
import com.example.acme_explorer.entity.MenuItem;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final ListView listView = root.findViewById(R.id.listView);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ArrayList<MenuItem> listMenu = MenuItem.getMenu();
                MenuAdapter adapter = new MenuAdapter(getContext(), listMenu);
                listView.setAdapter(adapter);
            }
        });
        return root;
    }
}
