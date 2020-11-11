package com.example.refugees.MainScreenFragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refugees.HelperClasses.NotificationAdapter;
import com.example.refugees.HelperClasses.Person;
import com.example.refugees.NotificationPackage.APIService;
import com.example.refugees.NotificationPackage.Client;
import com.example.refugees.NotificationPackage.Data;
import com.example.refugees.NotificationPackage.MyResponse;
import com.example.refugees.NotificationPackage.NotificationSender;
import com.example.refugees.NotificationPackage.Token;
import com.example.refugees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    Timer timer = new Timer();
    private View view;
    FirebaseDatabase database;
    DatabaseReference usersRef, requestsRef;
    ArrayList<String> keyArrayList;
    ArrayList<Person> personArrayList;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    ItemTouchHelper itemTouchHelper;
    String TAG_FRS = "FriendRequestStatus";
    TextView noRequest;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkNotifications();
            }
        }, 0, 5000);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String senderUser = personArrayList.get(position).getUser_id();
            final DatabaseReference receiver = database.getReference().child("users").child(currUser);
            final DatabaseReference sender = database.getReference().child("users").child(senderUser);

            switch (direction) {
                case ItemTouchHelper.RIGHT: {
                    sender.child("DetailsRequestsSent")
                            .child(currUser).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG_FRS, "sender req deleted");
                                        receiver.child("DetailsRequestsReceived")
                                                .child(senderUser).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG_FRS, "receiver req deleted");
                                                            personArrayList.remove(position);
                                                            adapter.notifyItemRemoved(position);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                    break;
                }

                case ItemTouchHelper.LEFT: {
                    receiver.child("HasAccessToMyInfo").child(senderUser).setValue("friend")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG_FRS, "sender req ACCEPTED");
                                        sender.child("DetailsRequestsSent")
                                                .child(currUser).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG_FRS, "sender req deleted");
                                                            receiver.child("DetailsRequestsReceived")
                                                                    .child(senderUser).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Log.d(TAG_FRS, "receiver req deleted");
                                                                                personArrayList.remove(position);
                                                                                adapter.notifyItemRemoved(position);


                                                                                FirebaseDatabase.getInstance().getReference().child("users")
                                                                                        .child(senderUser).child("device_token").child("token")
                                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                final String user_token = snapshot.getValue(String.class);
                                                                                                Log.d("notification_tracker", "got token " + user_token);
                                                                                                FirebaseDatabase.getInstance().getReference().child("users")
                                                                                                        .child(currUser).child("full_name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                        String senderName = snapshot.getValue(String.class);
                                                                                                        Log.d("notification_tracker", "got name " + senderName);
                                                                                                        sendNotifications(user_token, senderName,  "AR");
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });


                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                    break;
                }
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeRightActionIcon(R.drawable.ic_reject)
                    .addSwipeRightLabel(getString(R.string.reject))
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.green))
                    .addSwipeLeftActionIcon(R.drawable.ic_accept)
                    .addSwipeLeftLabel(getString(R.string.accept))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void initializeFields() {
        recycler = view.findViewById(R.id.notification_recycler);
        noRequest = view.findViewById(R.id.notifi_no_request);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycler);

        keyArrayList = new ArrayList<>();
        personArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference().child("users");
        requestsRef = database.getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("DetailsRequestsReceived");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    public void checkNotifications() {
        keyArrayList.clear();
        requestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        keyArrayList.add(ds.child("user_id").getValue(String.class));
                    }
                    Log.d("TAG", "Retrieve All Keys");
                    if (keyArrayList.size() > 0) {
                        noRequest.setVisibility(View.GONE);
                    } else {
                        noRequest.setVisibility(View.VISIBLE);
                    }
                    buildNotifications();

                } else {
                    Log.d("TAG", "Retrieve Abort");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buildNotifications() {
        personArrayList.clear();
        for (String key : keyArrayList) {
            usersRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Person person = new Person(
                                snapshot.child("image_url").getValue(String.class),
                                snapshot.child("full_name").getValue(String.class));
                        person.setUser_id(snapshot.child("user_id").getValue(String.class));
                        personArrayList.add(person);
                        Log.d("TAg", "exist");
                        adapter = new NotificationAdapter(personArrayList, getResources());
                        recycler.setAdapter(adapter);

                    } else {
                        Log.d("TAg", "Not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        Log.d("TAG_key", keyArrayList.get(0) + "");

    }


    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token").setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.d("notification_tracker", "Field");
                    } else if (response.body().success == 1) {
                        Log.d("notification_tracker", "Success");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


}