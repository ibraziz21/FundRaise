package com.redbravo.fundraise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<String> desc;



    public MyAdapter(Context context, ArrayList<String> campaign, ArrayList<String>desc){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = campaign;
        this.desc =desc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.listrow,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        String title = data.get(position);
        holder.textTitle.setText(title);

        String description = desc.get(position);
        holder.textDescription.setText(description);

    }


    @Override
    public int getItemCount() {


        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent=new Intent(getApplicationContext(),campaign.class);
                  intent.putExtra("title", data.get(getAdapterPosition()));
                  v.getContext().startActivity(intent);
                }
            });
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDesc);
        }
    }
}
