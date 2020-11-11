package com.example.refugees.HelperClasses;

import android.app.Application;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refugees.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyNotificationVH> {
    private ArrayList<Person> personArrayList;
    Resources resources;

    public NotificationAdapter(ArrayList<Person> personArrayList , Resources resources) {
        this.personArrayList = personArrayList;
        this.resources = resources;
    }

    @NonNull
    @Override
    public com.example.refugees.HelperClasses.NotificationAdapter.MyNotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler, parent, false);
        com.example.refugees.HelperClasses.NotificationAdapter.MyNotificationVH myVH = new com.example.refugees.HelperClasses.NotificationAdapter.MyNotificationVH(view);
        return myVH;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.refugees.HelperClasses.NotificationAdapter.MyNotificationVH holder, int position) {
        Person person = personArrayList.get(position);
        holder.name.setText(person.getFull_name()+ " " + resources.getString(R.string.notification_request_msg));
        Picasso.get().load(person.getImage_url()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }

    public static class MyNotificationVH extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name;
        public MyNotificationVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.notification_img);
            name = itemView.findViewById(R.id.notification_text);
        }

    }

}
