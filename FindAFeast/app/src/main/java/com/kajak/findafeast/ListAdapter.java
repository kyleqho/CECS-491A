package com.kajak.findafeast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imgid;
    private final Double[] rating;
    private final Double[] distance;

    public ListAdapter(Activity context, String[] itemname, String[] imgid,
                       Double[] rating, Double[] distance) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.rating = rating;
        this.distance = distance;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.restaurant_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtRating = (TextView) rowView.findViewById(R.id.restaurant_rating);
        TextView txtDistance = (TextView) rowView.findViewById(R.id.restaurant_distance);

        txtTitle.setText(itemname[position]);
        //txtRating.setText("Rating: "+rating[position].toString()+"/5");
        //txtDistance.setText("Distance: "+distance[position].toString()+" mi");
        Picasso
                .with(this.context)
                .load(imgid[position])
                .into(imageView);
        return rowView;

    };
}