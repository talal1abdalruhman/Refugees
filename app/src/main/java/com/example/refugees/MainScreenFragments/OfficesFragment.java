package com.example.refugees.MainScreenFragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.refugees.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

public class OfficesFragment extends Fragment implements View.OnClickListener {

    public OfficesFragment() {
        // Required empty public constructor
    }

    View view, views;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng userLocation;
    MaterialButton callBtn1, mailBtn1, mapBtn1, callBtn2, mailBtn2, mapBtn2, callBtn3, mapBtn3, mapBtn4, mapBtn5;

    public ArrayList<ConstraintLayout> layouts, descs, places, headers;
    public ArrayList<ImageView> arrows;
    public ArrayList<Boolean> states;
    public ArrayList<Float> places_save;
    public HashMap<View, Integer> map;
    public ScrollView scroller;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return views = view = inflater.inflate(R.layout.fragment_offices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ConstraintLayout layout = view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                setup();
            }
        });
        scroller = view.findViewById(R.id.scroller);
        places_save = new ArrayList<>();
        headers = new ArrayList<ConstraintLayout>();
        descs = new ArrayList<ConstraintLayout>();
        arrows = new ArrayList<ImageView>();
        places = new ArrayList<>();
        map = new HashMap<>();
        states = new ArrayList<>();

        headers.add(view.findViewById(R.id.branch1_header));
        headers.add(view.findViewById(R.id.branch2_header));
        headers.add(view.findViewById(R.id.branch3_header));
        headers.add(view.findViewById(R.id.branch4_header));
        headers.add(view.findViewById(R.id.branch5_header));

        places.add(view.findViewById(R.id.branch1_layout));
        places.add(view.findViewById(R.id.branch2_layout));
        places.add(view.findViewById(R.id.branch3_layout));
        places.add(view.findViewById(R.id.branch4_layout));
        places.add(view.findViewById(R.id.branch5_layout));

        descs.add(view.findViewById(R.id.branch1_desc));
        descs.add(view.findViewById(R.id.branch2_desc));
        descs.add(view.findViewById(R.id.branch3_desc));
        descs.add(view.findViewById(R.id.branch4_desc));
        descs.add(view.findViewById(R.id.branch5_desc));

        arrows.add(view.findViewById(R.id.branch1_arrow));
        arrows.add(view.findViewById(R.id.branch2_arrow));
        arrows.add(view.findViewById(R.id.branch3_arrow));
        arrows.add(view.findViewById(R.id.branch4_arrow));
        arrows.add(view.findViewById(R.id.branch5_arrow));

        for(int i = 0; i < places.size(); i++)
            states.add(false);
        for(int i = 0; i < headers.size(); i++)
            map.put(headers.get(i), i);
//        for(int i = 0; i < confirms.size(); i++)
//            map.put(confirms.get(i), i);

        ButtonInitialize();
    }

    public void setup() {
        for(int i = 0; i < descs.size(); i++) {
            descs.get(i).setPivotY(0);
            descs.get(i).setScaleY(0);
            descs.get(i).setAlpha(0);
        }
        places_save.add(0f);
        for(int i = 0; i < headers.size(); i++)
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = (ConstraintLayout)v;
                    int index = map.get(view);
                    Log.d("test", "testing " + index);
                    if(!states.get(index))
                        animate(index);
                    else
                        animate_back(index);
                }
            });
        int cumulative_sum = descs.get(0).getHeight();
        for(int i = 1; i < places.size(); i++) {
            places.get(i).setTranslationY(cumulative_sum * -1);
            cumulative_sum += descs.get(i).getHeight();
        }
        for(int i = 1; i < places.size(); i++) {
            places_save.add(places.get(i).getTranslationY());
            Log.d("testing this out ", "haha " + places_save.get(i) + " " + i);
        }
    }
    public void fix(int index) {
        scroller.setOnScrollChangeListener((ScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                int height = views.getHeight();
                int curr_bottom = scrollY + height;
                int condition = headers.get(0).getHeight() * headers.size() + descs.get(index).getHeight();
                if(curr_bottom >= condition) {
                    Log.d("test", "there we go " + curr_bottom + " " + condition + " " + index);
                    scroller.smoothScrollTo(0, scrollY);
                    ObjectAnimator.ofInt(scroller, "scrollY",  0).setDuration(1000).start();

                }
            }
        });
    }
    public void animate(int index) {
        for(int i = 0; i < places.size(); i++) {
            if(i == index)
                continue;
            if(states.get(i))
                animate_back(i);
        }
        states.set(index, true);
        arrows.get(index).animate().setDuration(300).rotation(180);
        descs.get(index).animate().setDuration(300).alpha(1);
        for(int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i) + descs.get(index).getHeight());
        }
        descs.get(index).animate().setDuration(300).scaleY(1);
    }

    public void animate_back(int index) {
        scroller.requestDisallowInterceptTouchEvent(true);
        scroller.smoothScrollTo(0, 0);
        states.set(index, false);
        arrows.get(index).animate().setDuration(300).rotation(0);
        descs.get(index).animate().setDuration(300).alpha(0);
        for(int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i));
        }
        descs.get(index).animate().setDuration(300).scaleY(0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.branch1_call_btn: {
                Call("0096265302000");
                break;
            }
            case R.id.branch1_mail_btn: {
                SendMail("joram@unhcr.org");
                break;
            }
            case R.id.branch1_map_btn: {
                LatLng latLng = new LatLng(31.99639758434624, 35.8405583072897);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    AskForLocationPermission();
                } else {
                    ShowInMap(latLng);
                }
                break;
            }

            case R.id.branch2_call_btn: {
                Call("0096227201900");
                break;
            }
            case R.id.branch2_mail_btn: {
                SendMail("jorir@unhcr.org");
                break;
            }
            case R.id.branch2_map_btn: {
                LatLng latLng = new LatLng(32.53933848086828, 35.833021789187136);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    AskForLocationPermission();
                } else {
                    ShowInMap(latLng);
                }
                break;
            }

            case R.id.branch3_call_btn: {
                Call("0096226296000");
                break;
            }
            case R.id.branch4_map_btn:
            case R.id.branch3_map_btn: {
                LatLng latLng = new LatLng(32.33586072795338, 36.220505457009764);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    AskForLocationPermission();
                } else {
                    ShowInMap(latLng);
                }
                break;
            }

            case R.id.branch5_map_btn: {
                LatLng latLng = new LatLng(31.908096289938502, 36.577466492571965);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    AskForLocationPermission();
                } else {
                    ShowInMap(latLng);
                }
                break;
            }
        }
    }

    public void ButtonInitialize() {
        callBtn1 = view.findViewById(R.id.branch1_call_btn);
        callBtn1.setOnClickListener(this);
        mailBtn1 = view.findViewById(R.id.branch1_mail_btn);
        mailBtn1.setOnClickListener(this);
        mapBtn1 = view.findViewById(R.id.branch1_map_btn);
        mapBtn1.setOnClickListener(this);
        callBtn2 = view.findViewById(R.id.branch2_call_btn);
        callBtn2.setOnClickListener(this);
        mailBtn2 = view.findViewById(R.id.branch2_mail_btn);
        mailBtn2.setOnClickListener(this);
        mapBtn2 = view.findViewById(R.id.branch2_map_btn);
        mapBtn2.setOnClickListener(this);
        callBtn3 = view.findViewById(R.id.branch3_call_btn);
        callBtn3.setOnClickListener(this);
        mapBtn3 = view.findViewById(R.id.branch3_map_btn);
        mapBtn3.setOnClickListener(this);
        mapBtn4 = view.findViewById(R.id.branch4_map_btn);
        mapBtn4.setOnClickListener(this);
        mapBtn5 = view.findViewById(R.id.branch5_map_btn);
        mapBtn5.setOnClickListener(this);
    }

    public void Call(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phone)));
    }

    public void SendMail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }

    public void ShowInMap(LatLng targetLocation) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        } else {
            Log.d("maplink", "here1");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                Log.d("maplink", "here2");
                                Toast.makeText(getContext(), "Found Location", Toast.LENGTH_LONG).show();
                                Location location = (Location) task.getResult();
                                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                String uri = "http://maps.google.com/maps?saddr="
                                        + userLocation.latitude + "," + userLocation.longitude
                                        + "&daddr=" + targetLocation.latitude + "," + targetLocation.longitude;
                                Log.d("maplink", uri);
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "Didn't Found Location", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    public void AskForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Log.d("permission", "AskForLocationPermission: asked ");
        } else {
            Log.e("permission", "PERMISSION GRANTED");
        }
    }

    public void AskToEnableGPS() {
        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(getContext())
                        .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), REQUEST_CHECK_CODE);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            } catch (ClassCastException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("permission", "onRequestPermissionsResult: returned" + requestCode);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "onRequestPermissionsResult: accepted");
                AskToEnableGPS();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}