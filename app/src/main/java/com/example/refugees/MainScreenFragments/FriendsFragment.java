package com.example.refugees.MainScreenFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.example.refugees.HelperClasses.Address;
import com.example.refugees.HelperClasses.Person;
import com.example.refugees.HelperClasses.UsersSearchAdapter;
import com.example.refugees.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.refugees.MainScreenActivity.navView;

public class FriendsFragment extends Fragment implements UsersSearchAdapter.OnPersonListener {

    public FriendsFragment() {
        // Required empty public constructor
    }

    View rootView;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    ArrayList<Person> friends = new ArrayList<>();
    FirebaseUser currentUser;
    DatabaseReference mRef, tmpRef;
    CircleImageView friendImg;
    TextView friendName, friendEmail, friendPhone, friendAddress;
    ConstraintLayout rootLayout, targetCard;
    String targetId, phoneNo, mailAddress;
    boolean isTargetCardOpen = false;
    CircularProgressButton callBtn, sendMailBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        InitializeFields();
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadFriends();

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNo));
                startActivity(intent);
            }
        });

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mailAddress, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Write your message");
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTargetCardOpen) {
                    recycler.setVisibility(View.VISIBLE);
                    recycler.setAdapter(adapter);
                    targetCard.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_translate_down_anim));
                    targetCard.animate();
                    targetCard.setVisibility(View.GONE);
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isTargetCardOpen) {
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(adapter);
                            targetCard.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_translate_down_anim));
                            targetCard.animate();
                            targetCard.setVisibility(View.GONE);
                            isTargetCardOpen = false;
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_friends_to_home);
                            navView.setCheckedItem(R.id.home);
                        }
                    }
                });
    }

    private void InitializeFields() {
        recycler = rootView.findViewById(R.id.friends_result);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("HasAccessToTheirInfo");
        tmpRef = FirebaseDatabase.getInstance().getReference("users");

        friendImg = rootView.findViewById(R.id.friends_img);
        friendName = rootView.findViewById(R.id.friends_name);
        friendEmail = rootView.findViewById(R.id.friends_email);
        friendPhone = rootView.findViewById(R.id.friends_phone);
        friendAddress = rootView.findViewById(R.id.friends_address);
        targetCard = rootView.findViewById(R.id.friends_target_card);
        rootLayout = rootView.findViewById(R.id.root_layout);
        callBtn = rootView.findViewById(R.id.friends_call);
        sendMailBtn = rootView.findViewById(R.id.friends_send_email);
    }

    private void LoadFriends() {
        mRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Log.d("load_friends", "snapShot exists");
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        final String[] info = new String[2];
                        tmpRef.child(snap.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                info[1] = snapshot.child("full_name").getValue().toString();
                                info[0] = snapshot.child("image_url").getValue().toString();
                                Log.d("load_friends", info[1]);
                                Log.d("load_friends", info[0]);
                                Person person = new Person(info[0], info[1]);
                                person.setUser_id(snap.getValue().toString());
                                friends.add(person);
                                adapter = new UsersSearchAdapter(friends, FriendsFragment.this);
                                recycler.setAdapter(adapter);
                                Log.d("load_friends", adapter.getItemCount() + "");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }

                } else {
                    Log.d("load_friends", "snapShot not exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void OnPersonClickedListener(int position) {
        targetId = friends.get(position).getUser_id();
        tmpRef.child(targetId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Picasso.get().load(snapshot.child("image_url").getValue().toString()).into(friendImg);
                    friendName.setText(snapshot.child("full_name").getValue().toString());
                    friendEmail.setText(mailAddress = snapshot.child("email").getValue().toString());
                    friendPhone.setText(phoneNo = snapshot.child("phone_no").getValue().toString());
                    Address address = snapshot.child("address").getValue(Address.class);
                    friendAddress.setText(address.toString());
                    targetCard.setVisibility(View.VISIBLE);
                    isTargetCardOpen = true;
                    targetCard.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_translate_up_anim));
                    targetCard.animate();
                    recycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}