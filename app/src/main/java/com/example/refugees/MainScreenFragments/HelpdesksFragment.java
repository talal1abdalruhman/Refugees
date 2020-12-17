package com.example.refugees.MainScreenFragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpdesksFragment newInstance} factory method to
 * create an instance of this fragment.
 */

public class HelpdesksFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view, views;
    public ArrayList<ConstraintLayout> layouts, descs, places, headers;
    public ArrayList<ImageView> arrows;
    public ArrayList<Boolean> states;
    public ArrayList<Float> places_save;
    public HashMap<View, Integer> map;
    public ScrollView scroller;

    public HelpdesksFragment() {
        // Required empty public constructor
    }


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng userLocation;
    MaterialButton mapBtn1, mapBtn2, mapBtn3, mapBtn4, mapBtn5, mapBtn6, mapBtn7, mapBtn8, mapBtn9, mapBtn10, mapBtn11, mapBtn12, mapBtn13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return views = view = inflater.inflate(R.layout.fragment_helpdesks, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helpdesk1_map_btn: {
                LatLng latLng = new LatLng(31.877000032342707, 35.99576222382696);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk2_map_btn: {
                LatLng latLng = new LatLng(31.977831078366318, 35.92217319600955);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk3_map_btn: {
                LatLng latLng = new LatLng(32.088492727579826, 36.09816803755059);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk4_map_btn: {
                LatLng latLng = new LatLng(31.730189767617183, 35.793437238254064);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk5_map_btn: {
                LatLng latLng = new LatLng(32.06423636035091, 35.72267742356779);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk6_map_btn: {
                LatLng latLng = new LatLng(31.861958281819124, 35.64031232362207);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk7_map_btn: {
                LatLng latLng = new LatLng(30.218663689408487, 35.737831513196454);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk8_map_btn: {
                LatLng latLng = new LatLng(31.187296642252473, 35.70248920855095);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk9_map_btn: {
                LatLng latLng = new LatLng(29.520525516402298, 35.00794194886785);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk10_map_btn: {
                LatLng latLng = new LatLng(31.971651298113027, 35.881997921284565);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk11_map_btn: {
                LatLng latLng = new LatLng(32.32327688888242, 35.75154319530603);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk12_map_btn: {
                LatLng latLng = new LatLng(32.2758915210928, 35.89649291548452);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk13_map_btn: {
                LatLng latLng = new LatLng(32.533898962179734, 35.82582818536365);
                ShowInMap(latLng);
                break;
            }
        }
    }

    public void ButtonInitialize() {
        mapBtn1 = view.findViewById(R.id.helpdesk1_map_btn);
        mapBtn1.setOnClickListener(this);
        mapBtn2 = view.findViewById(R.id.helpdesk2_map_btn);
        mapBtn2.setOnClickListener(this);
        mapBtn3 = view.findViewById(R.id.helpdesk3_map_btn);
        mapBtn3.setOnClickListener(this);
        mapBtn4 = view.findViewById(R.id.helpdesk4_map_btn);
        mapBtn4.setOnClickListener(this);
        mapBtn5 = view.findViewById(R.id.helpdesk5_map_btn);
        mapBtn5.setOnClickListener(this);
        mapBtn6 = view.findViewById(R.id.helpdesk6_map_btn);
        mapBtn6.setOnClickListener(this);
        mapBtn7 = view.findViewById(R.id.helpdesk7_map_btn);
        mapBtn7.setOnClickListener(this);
        mapBtn8 = view.findViewById(R.id.helpdesk8_map_btn);
        mapBtn8.setOnClickListener(this);
        mapBtn9 = view.findViewById(R.id.helpdesk9_map_btn);
        mapBtn9.setOnClickListener(this);
        mapBtn10 = view.findViewById(R.id.helpdesk10_map_btn);
        mapBtn10.setOnClickListener(this);
        mapBtn11 = view.findViewById(R.id.helpdesk11_map_btn);
        mapBtn11.setOnClickListener(this);
        mapBtn12 = view.findViewById(R.id.helpdesk12_map_btn);
        mapBtn12.setOnClickListener(this);
        mapBtn13 = view.findViewById(R.id.helpdesk13_map_btn);
        mapBtn13.setOnClickListener(this);
    }

    public void ShowInMap(LatLng targetLocation) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AskForLocationPermission();
        } else {
            Log.d("maplink", "here1");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                Log.d("maplink", "here2");
                                //Toast.makeText(getContext(), "Found Location", Toast.LENGTH_LONG).show();
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
                                //Toast.makeText(getContext(), "Didn't Found Location", Toast.LENGTH_LONG).show();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ConstraintLayout layout = view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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

        headers.add(view.findViewById(R.id.helpdesk1_header));
        headers.add(view.findViewById(R.id.helpdesk2_header));
        headers.add(view.findViewById(R.id.helpdesk3_header));
        headers.add(view.findViewById(R.id.helpdesk4_header));
        headers.add(view.findViewById(R.id.helpdesk5_header));
        headers.add(view.findViewById(R.id.helpdesk6_header));
        headers.add(view.findViewById(R.id.helpdesk7_header));
        headers.add(view.findViewById(R.id.helpdesk8_header));
        headers.add(view.findViewById(R.id.helpdesk9_header));
        headers.add(view.findViewById(R.id.helpdesk10_header));
        headers.add(view.findViewById(R.id.helpdesk11_header));
        headers.add(view.findViewById(R.id.helpdesk12_header));
        headers.add(view.findViewById(R.id.helpdesk13_header));

        places.add(view.findViewById(R.id.helpdesk1_layout));
        places.add(view.findViewById(R.id.helpdesk2_layout));
        places.add(view.findViewById(R.id.helpdesk3_layout));
        places.add(view.findViewById(R.id.helpdesk4_layout));
        places.add(view.findViewById(R.id.helpdesk5_layout));
        places.add(view.findViewById(R.id.helpdesk6_layout));
        places.add(view.findViewById(R.id.helpdesk7_layout));
        places.add(view.findViewById(R.id.helpdesk8_layout));
        places.add(view.findViewById(R.id.helpdesk9_layout));
        places.add(view.findViewById(R.id.helpdesk10_layout));
        places.add(view.findViewById(R.id.helpdesk11_layout));
        places.add(view.findViewById(R.id.helpdesk12_layout));
        places.add(view.findViewById(R.id.helpdesk13_layout));

        descs.add(view.findViewById(R.id.helpdesk1_desc));
        descs.add(view.findViewById(R.id.helpdesk2_desc));
        descs.add(view.findViewById(R.id.helpdesk3_desc));
        descs.add(view.findViewById(R.id.helpdesk4_desc));
        descs.add(view.findViewById(R.id.helpdesk5_desc));
        descs.add(view.findViewById(R.id.helpdesk6_desc));
        descs.add(view.findViewById(R.id.helpdesk7_desc));
        descs.add(view.findViewById(R.id.helpdesk8_desc));
        descs.add(view.findViewById(R.id.helpdesk9_desc));
        descs.add(view.findViewById(R.id.helpdesk10_desc));
        descs.add(view.findViewById(R.id.helpdesk11_desc));
        descs.add(view.findViewById(R.id.helpdesk12_desc));
        descs.add(view.findViewById(R.id.helpdesk13_desc));

        arrows.add(view.findViewById(R.id.helpdesk1_arrow));
        arrows.add(view.findViewById(R.id.helpdesk2_arrow));
        arrows.add(view.findViewById(R.id.helpdesk3_arrow));
        arrows.add(view.findViewById(R.id.helpdesk4_arrow));
        arrows.add(view.findViewById(R.id.helpdesk5_arrow));
        arrows.add(view.findViewById(R.id.helpdesk6_arrow));
        arrows.add(view.findViewById(R.id.helpdesk7_arrow));
        arrows.add(view.findViewById(R.id.helpdesk8_arrow));
        arrows.add(view.findViewById(R.id.helpdesk9_arrow));
        arrows.add(view.findViewById(R.id.helpdesk10_arrow));
        arrows.add(view.findViewById(R.id.helpdesk11_arrow));
        arrows.add(view.findViewById(R.id.helpdesk12_arrow));
        arrows.add(view.findViewById(R.id.helpdesk13_arrow));

        for (int i = 0; i < places.size(); i++)
            states.add(false);
        for (int i = 0; i < headers.size(); i++)
            map.put(headers.get(i), i);

    }
    @SuppressLint("ClickableViewAccessibility")
    public void setup() {
        for (int i = 0; i < descs.size(); i++) {
            descs.get(i).setPivotY(0);
            descs.get(i).setScaleY(0);
            descs.get(i).setAlpha(0);
        }
        places_save.add(0f);
        for (int i = 0; i < headers.size(); i++)
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = (ConstraintLayout) v;
                    int index = map.get(view);
                    if (!states.get(index))
                        animate(index);
                    else
                        animate_back(index);
                }
            });
        int cumulative_sum = descs.get(0).getHeight();
        for (int i = 1; i < places.size(); i++) {
            places.get(i).setTranslationY(cumulative_sum * -1);
            cumulative_sum += descs.get(i).getHeight();
        }
        for (int i = 1; i < places.size(); i++) {
            places_save.add(places.get(i).getTranslationY());
            Log.d("testing this out ", "haha " + places_save.get(i) + " " + i);
        }
        fix(0);
    }
    public void fix(int index) {
        scroller.setOnScrollChangeListener((ScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                int height = views.getHeight();
                int curr_bottom = scrollY + height;
                int condition = headers.get(0).getHeight() * headers.size() + descs.get(index).getHeight();
                if(curr_bottom >= condition) {
                    Log.d("test", "there we go " + curr_bottom + " " + condition + " " + index);
                    scroller.fling(1);
                    scroller.smoothScrollTo(0, scrollY);
                    ObjectAnimator.ofInt(scroller, "scrollY",  0).setDuration(1000).start();

                }
            }
        });
    }
    public void animate(int index) {
        fix(index);
        for (int i = 0; i < places.size(); i++) {
            if (i == index)
                continue;
            if (states.get(i))
                animate_back(i);
        }
        states.set(index, true);
        arrows.get(index).animate().setDuration(300).rotation(180);
        descs.get(index).animate().setDuration(300).alpha(1);
        for (int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i) + descs.get(index).getHeight());
        }
        descs.get(index).animate().setDuration(300).scaleY(1);
    }
    public void animate_back(int index) {
        fix(0);
        scroller.smoothScrollTo(0, 0);
        states.set(index, false);
        arrows.get(index).animate().setDuration(300).rotation(0);
        descs.get(index).animate().setDuration(300).alpha(0);
        for (int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i));
        }
        descs.get(index).animate().setDuration(300).scaleY(0);
    }
}