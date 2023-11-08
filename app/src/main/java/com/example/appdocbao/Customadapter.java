package com.example.appdocbao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Customadapter extends ArrayAdapter<Docbao> {

    public Customadapter(Context context, int resource, List<Docbao> items){
        super(context,resource,items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null){
            LayoutInflater inflater= LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.dong_layout_listview, null);

        }
        Docbao p= getItem(position);
        if(p!=null){
            TextView txttitle=(TextView) view.findViewById(R.id.textviewtitle);
            txttitle.setText(p.title);

            ImageView imageView= (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(p.image).into(imageView);
        }
        return  view;
    }
}
