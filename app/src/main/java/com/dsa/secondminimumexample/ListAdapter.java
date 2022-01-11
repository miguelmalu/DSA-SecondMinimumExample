package com.dsa.secondminimumexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dsa.secondminimumexample.API.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<User> data;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<User> userList, Context context) {
        this.data = userList;
        this.mInflater = LayoutInflater.from((Context) context);
        this.context = (Context) context;
    }


    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.activity_users_list, null);
        return new ListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItems(List<User> items){
        data=items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView username;

        MyViewHolder(View itemView){
            super(itemView);
            image=itemView.findViewById(R.id.iconImagenView);
            username=itemView.findViewById(R.id.NameTextView);
        }

        void bindData(final User user){
            username.setText(user.getLogin());
            Picasso.get().load(user.getAvatar_url()).into(image);
        }

    }
}
