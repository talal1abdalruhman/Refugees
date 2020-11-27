package com.example.refugees.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refugees.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersSearchAdapter extends RecyclerView.Adapter<UsersSearchAdapter.MyVH> {
    private ArrayList<Person> personArrayList;
    private OnPersonListener onPersonListener;
    private Context mContext;

    public UsersSearchAdapter(ArrayList<Person> personArrayList, OnPersonListener onPersonListener) {
        this.personArrayList = personArrayList;
        this.onPersonListener = onPersonListener;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friend_recycler, parent, false);
        MyVH myVH = new MyVH(view, onPersonListener);
        return myVH;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_anim));
        Person person = personArrayList.get(position);
        holder.name.setText(person.getFull_name());
        Picasso.get().load(person.getImage_url()).into(holder.img);
        holder.img.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_translate_anim));
        holder.name.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_translate_anim));
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }

    public static class MyVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img;
        TextView name;
        LinearLayout container;
        OnPersonListener onPersonListener;
        public MyVH(@NonNull View itemView, OnPersonListener onPersonListener) {
            super(itemView);
            img = itemView.findViewById(R.id.recycler_img);
            name = itemView.findViewById(R.id.recycler_text);
            container = itemView.findViewById(R.id.container);
            this.onPersonListener = onPersonListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onPersonListener.OnPersonClickedListener(getAdapterPosition());
        }
    }

    public interface OnPersonListener {
        public void OnPersonClickedListener(int position);
    }
}
