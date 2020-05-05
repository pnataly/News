package com.example.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.model.Item;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.itemViewHolder> {
    private Context context;
    private List<Item> itemsList;

    public RecyclerViewAdapter(Context context, List<Item> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row, viewGroup, false);
        return new itemViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        Item item = itemsList.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.link.setText(item.getLink());
        holder.time.setText(item.getTime());
        holder.image.setImageBitmap(item.getImage());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        private TextView link;
        private TextView time;
        private ImageView image;

        public itemViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            link = view.findViewById(R.id.link);
            time = view.findViewById(R.id.time);
            image = view.findViewById(R.id.image);
            link.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(itemsList.get(getAdapterPosition()).getLink());
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(linkIntent);
        }
    }
}
