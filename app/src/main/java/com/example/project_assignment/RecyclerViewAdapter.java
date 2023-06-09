package com.example.project_assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final List<Meteorite> meteorites;
    private final Context context;

    public RecyclerViewAdapter(Context context, List<Meteorite> meteorites) {
        this.context = context;
        this.meteorites = meteorites;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item, parent, false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.meteoriteName.setText(meteorites.get(position).getName());
        holder.meteoriteMass.setText(meteorites.get(position).getMass() + "kg");
        holder.MeteoriteAge.setText(meteorites.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return meteorites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView meteoriteName, meteoriteMass, MeteoriteAge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            meteoriteName = itemView.findViewById(R.id.textview_meteoriteName);
            meteoriteMass = itemView.findViewById(R.id.textview_meteoriteMass);
            MeteoriteAge = itemView.findViewById(R.id.textview_meteoriteAge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MeteoriteActivity.class);

                    Meteorite meteorite = meteorites.get(getAdapterPosition());
                    intent.putExtra("meteorite", meteorite);

                    context.startActivity(intent);
                }
            });
        }
    }
}