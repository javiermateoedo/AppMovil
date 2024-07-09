package com.plasticrangers.frontend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class ListAdapter extends ArrayAdapter<NewsData> {
    public ListAdapter(@NonNull Context context, ArrayList<NewsData> dataArrayList) {
        super(context, R.layout.list_news, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        NewsData listData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);

        listImage.setImageResource(listData.getImagen());
        listName.setText(listData.getNombre());
        return view;
    }
}