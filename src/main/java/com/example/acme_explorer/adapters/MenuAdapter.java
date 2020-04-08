package com.example.acme_explorer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acme_explorer.R;
import com.example.acme_explorer.entity.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MenuItem> itemsMenu;

    public MenuAdapter(Context context, ArrayList<MenuItem> itemsMenu) {
        this.context = context;
        this.itemsMenu = itemsMenu;
    }

    @Override
    public int getCount() {
        return itemsMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.menu_item, parent, false);
        }

        // get current item to be displayed
        final MenuItem currentItem = (MenuItem) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItem = (TextView)
                convertView.findViewById(R.id.textViewMenu);
        ImageView imgageView = (ImageView)
                convertView.findViewById(R.id.imageViewMenu);

        textViewItem.setText(currentItem.getDescription());
        imgageView.setImageResource(currentItem.getImageId());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context ,currentItem.getaClass());
                context.startActivity(intent);
            }
        });
        // returns the view for the current row
        return convertView;
    }
}
