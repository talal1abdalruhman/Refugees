package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.example.refugees.HelperClasses.Address;
import com.example.refugees.HelperClasses.Person;
import com.example.refugees.HelperClasses.UsersSearchAdapter;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.refugees.MainScreenActivity.navView;

public class SearchFragment extends Fragment implements UsersSearchAdapter.OnPersonListener{

    public SearchFragment() {
        // Required empty public constructor
    }

    View view;
    EditText searchField;
    ImageButton searchbtn;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    ArrayList<Person> personArrayList = new ArrayList<>();
    LinearLayout targetCard, mainLayout, allInfoLayout;
    CircleImageView targetImg;
    TextView targetName, targetEmail, targetPhone, targetAddress;
    Button infoReq;
    String clickedId;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeFields();
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchField.getText().toString();
                personArrayList.clear();
                searchForSomeone(text);
            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetCard.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }
        });

        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetCard.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }
        });

        infoReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoReq.getText().toString().equals(getString(R.string.request_for_info))) {
                    SendRequest();
                } else {
                    CancelRequest();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_home);
                navView.setCheckedItem(R.id.home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }


    private void InitializeFields() {
        searchField = view.findViewById(R.id.tv_input);
        searchbtn = view.findViewById(R.id.search_btn);
        recycler = view.findViewById(R.id.search_result);

        targetImg = view.findViewById(R.id.search_target_img);
        targetName = view.findViewById(R.id.search_target_name);
        targetEmail = view.findViewById(R.id.search_email);
        targetPhone = view.findViewById(R.id.search_phone);
        targetAddress = view.findViewById(R.id.search_address);
        infoReq = view.findViewById(R.id.search_request_info_btn);
        targetCard = view.findViewById(R.id.search_target_card);
        mainLayout = view.findViewById(R.id.search_main_layout);
        allInfoLayout = view.findViewById(R.id.search_all_info);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


    }

    private void searchForSomeone(String text) {
        final String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        text = text.trim();

        if(text.isEmpty()){
            //TODO: do something
            return;
        }
        String finalText = text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
        ref.orderByChild("full_name").startAt(finalText).endAt(finalText.toLowerCase()+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Log.d("founded", userSnapshot.child("full_name").getValue().toString());

                        if (userSnapshot.child("searchable").getValue().toString().equals("true")) {
                            // Handling for "if user does NOT upload photo"
                            String imgUrl = userSnapshot.child("image_url").getValue().toString();

                            Person person = new Person(imgUrl, userSnapshot.child("full_name").getValue().toString());
                            person.setUser_id(userSnapshot.child("user_id").getValue().toString());

                            // Handling for "User find himself in result"
                            if (!currentUId.equals(person.getUser_id())
                                    && person.getFull_name().toLowerCase().contains(finalText.toLowerCase())) {
                                personArrayList.add(person);
                            }
                        }

                    }
                    adapter = new UsersSearchAdapter(personArrayList, SearchFragment.this);
                    recycler.setAdapter(adapter);
                } else {
                    Log.d("founded","NOT founded");
                    personArrayList.clear();
                    adapter = new UsersSearchAdapter(personArrayList, SearchFragment.this);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("founded", "Error");
                throw databaseError.toException();
            }
        });
    }

    private void SendRequest() {
        final String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference();
        receiverRef.child("users").child(clickedId).child("DetailsRequestsReceived")
                .child(currentUId).child("user_id").setValue(currentUId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference();
                            senderRef.child("users").child(currentUId).child("DetailsRequestsSent")
                                    .child(clickedId).child("user_id").setValue(clickedId)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                FirebaseDatabase.getInstance().getReference().child("users")
                                                        .child(clickedId).child("device_token").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        final String usertoken=snapshot.getValue(String.class);
                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                .child(currentUId).child("full_name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String senderName = snapshot.getValue(String.class);
                                                                sendNotifications(usertoken, senderName, "SR");
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
                                                UpdateToken();
                                                Log.d("request", "sent");
                                                infoReq.setText(R.string.cancel_info_request);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void CancelRequest() {
        final String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference();
        receiverRef.child("users").child(clickedId).child("DetailsRequestsReceived")
                .child(currentUId).child("user_id").removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference();
                            senderRef.child("users").child(currentUId).child("DetailsRequestsSent")
                                    .child(clickedId).child("user_id").removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("request", "canceled");
                                                infoReq.setText(R.string.request_for_info);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void OnPersonClickedListener(int position) {

        clickedId = personArrayList.get(position).getUser_id();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(clickedId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Picasso.get().load(snapshot.child("image_url").getValue().toString()).into(targetImg);
                    targetName.setText(snapshot.child("full_name").getValue().toString());
                    targetEmail.setText(snapshot.child("email").getValue().toString());
                    Address address = new Address(
                            snapshot.child("address").child("governorate").getValue().toString(),
                            snapshot.child("address").child("city").getValue().toString());
                    targetAddress.setText(address.toString());
                    String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (snapshot.child("HasAccessToMyInfo").hasChild(currUser)) {
                        allInfoLayout.setVisibility(View.VISIBLE);
                        infoReq.setVisibility(View.GONE);
                    } else {
                        allInfoLayout.setVisibility(View.GONE);
                        infoReq.setVisibility(View.VISIBLE);
                    }
                    recycler.setVisibility(View.GONE);
                    targetCard.setVisibility(View.VISIBLE);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
                    currRef.child("DetailsRequestsSent").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(clickedId)) {
                                infoReq.setText(R.string.cancel_info_request);
                            } else {
                                infoReq.setText(R.string.request_for_info);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
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
                    }else if(response.body().success == 1) {
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