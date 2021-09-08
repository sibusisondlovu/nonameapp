package co.za.jaspasystems.nonameapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.za.jaspasystems.nonameapp.R;
import co.za.jaspasystems.nonameapp.activities.HamperDetailsActivity;
import co.za.jaspasystems.nonameapp.models.Hamper;

public class HamperAdapter extends Adapter<HamperAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Hamper> hamperArrayList;

    public HamperAdapter(Context context, ArrayList<Hamper> hamperArrayList) {
        this.context = context;
        this.hamperArrayList = hamperArrayList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public HamperAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_hamper,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull HamperAdapter.MyViewHolder holder, int position) {
        Hamper hamper = hamperArrayList.get(position);

        holder.name.setText(hamper.getName());
        holder.price.setText("R " +  hamper.getPrice());
        Glide.with(context).load( hamper.getImage()).into(holder.image);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HamperDetailsActivity.class);
                intent.putExtra("name", hamper.getName());
                intent.putExtra("contents", hamper.getContents());
                intent.putExtra("price", hamper.getPrice());
                intent.putExtra("image", hamper.getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hamperArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, price;
        ImageView image, more;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.layout_tv_hamperName);
            price = itemView.findViewById(R.id.layout_tv_hamperPrice);
            image = itemView.findViewById(R.id.layout_iv_hamperPhoto);
            more  = itemView.findViewById(R.id.layout_iv_more);
        }
    }
}
