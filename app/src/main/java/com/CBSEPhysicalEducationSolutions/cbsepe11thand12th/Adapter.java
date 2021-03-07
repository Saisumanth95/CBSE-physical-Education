package com.CBSEPhysicalEducationSolutions.cbsepe11thand12th;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Item> mList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView titleText;
        public TextView descText;
        public ImageView imageView;
        public ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_load_photo);
            titleText = itemView.findViewById(R.id.title);
            descText = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.image);

            progressBar.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener != null){

                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }

                }
            });


        }
    }


    public Adapter(ArrayList<Item> list,Context context){

        mList = list;
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        MyViewHolder mv = new MyViewHolder(v,listener);

        return mv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Item currentItem = mList.get(position);

        holder.titleText.setText(currentItem.getTitle());

        holder.descText.setText(currentItem.getDesc());

        Picasso.with(context).load(currentItem.getImage()).error(R.drawable.pelogo).fit().into(holder.imageView, new Callback() {

            @Override
            public void onSuccess() {

                holder.progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError() {

                holder.progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError: download failed");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }




}
