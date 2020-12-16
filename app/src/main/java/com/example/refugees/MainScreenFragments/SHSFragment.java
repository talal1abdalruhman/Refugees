package com.example.refugees.MainScreenFragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SHSFragment extends Fragment implements View.OnClickListener {

    public SHSFragment() {
        // Required empty public constructor
    }

    View view;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLocation;
    private MaterialButton mapBtn1, mapBtn2, mapBtn3, mapBtn4, mapBtn5,
            mapBtn6, mapBtn7, mapBtn8, mapBtn9, mapBtn10,
            mapBtn11, mapBtn12, mapBtn13, mapBtn14, mapBtn15,
            mapBtn16, mapBtn17, mapBtn18, mapBtn19, mapBtn20,
            mapBtn21, mapBtn22;
    private MaterialButton callBtn1, callBtn2, callBtn3, callBtn4, callBtn5,
            callBtn6, callBtn7, callBtn8, callBtn9, callBtn10,
            callBtn11, callBtn12, callBtn13, callBtn14, callBtn15,
            callBtn16, callBtn17, callBtn18, callBtn19, callBtn20,
            callBtn21, callBtn22;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_s_h_s, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButtonInitialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.community1_map_btn:{
                LatLng latLng = new LatLng(31.944676105045875, 35.932725831860466);
                ShowInMap(latLng);
                break;
            }
            case R.id.community1_call_btn:{
                Call("064799901");
                break;
            }
            case R.id.community2_map_btn:{
                LatLng latLng = new LatLng(31.97051147586422, 35.9582306900676);
                ShowInMap(latLng);
                break;
            }
            case R.id.community2_call_btn:{
                Call("064918601");
                break;
            }
            case R.id.community3_map_btn:{
                LatLng latLng = new LatLng(32.34471157314119, 36.213049159208474);
                ShowInMap(latLng);
                break;
            }
            case R.id.community3_call_btn:{
                Call("026230209");
                break;
            }
            case R.id.community4_map_btn:{
                LatLng latLng = new LatLng(32.551992005766394, 35.8514312104299);
                ShowInMap(latLng);
                break;
            }
            case R.id.community4_call_btn:{
                Call("027011146");
                break;
            }
            case R.id.community5_map_btn:{
                LatLng latLng = new LatLng(32.28211898433947, 35.89507786615382);
                ShowInMap(latLng);
                break;
            }
            case R.id.community5_call_btn:{
                Call("0770439738");
                break;
            }
            case R.id.community6_map_btn:{
                LatLng latLng = new LatLng(32.06772795830187, 36.09077536729851);
                ShowInMap(latLng);
                break;
            }
            case R.id.community6_call_btn: {
                Call("053936066");
                break;
            }
            case R.id.community7_map_btn:{
                LatLng latLng = new LatLng(31.1849583044237, 35.70651271153977);
                ShowInMap(latLng);
                break;
            }
            case R.id.community7_call_btn: {
                Call("0776736487");
                break;
            }
            case R.id.community8_map_btn:{
                LatLng latLng = new LatLng(31.95001030091659, 35.929783755811094);
                ShowInMap(latLng);
                break;
            }
            case R.id.community8_call_btn:{
                Call("0791535688");
                break;
            }
            case R.id.community9_map_btn:{
                LatLng latLng = new LatLng(32.06531398495448, 35.84134567817633);
                ShowInMap(latLng);
                break;
            }
            case R.id.community9_call_btn:{
                Call("0790218986");
                break;
            }
            case R.id.community10_map_btn:{
                LatLng latLng = new LatLng(32.08168655686508, 36.07780554992606);
                ShowInMap(latLng);
                break;
            }
            case R.id.community10_call_btn:{
                Call("0799495313");
                break;
            }
            case R.id.community11_map_btn:{
                LatLng latLng = new LatLng(32.34805800498357, 36.21609810620442);
                ShowInMap(latLng);
                break;
            }
            case R.id.community11_call_btn:{
                Call("0790305389");
                break;
            }
            case R.id.community12_map_btn:{
                LatLng latLng = new LatLng(32.51147128785113, 35.89519719521884);
                ShowInMap(latLng);
                break;
            }
            case R.id.community12_call_btn:{
                Call("0795004792");
                break;
            }
            case R.id.community13_map_btn:{
                LatLng latLng = new LatLng(32.59146011622774, 35.88330493786754);
                ShowInMap(latLng);
                break;
            }
            case R.id.community13_call_btn:{
                Call("0790037124");
                break;
            }
            case R.id.community14_map_btn:{
                LatLng latLng = new LatLng(32.54230474657122, 36.00710132139159);
                ShowInMap(latLng);
                break;
            }
            case R.id.community14_call_btn:{
                Call("0795004792");
                break;
            }
            case R.id.community15_map_btn:{
                LatLng latLng = new LatLng(32.35842529769317, 35.785489057430475);
                ShowInMap(latLng);
                break;
            }
            case R.id.community15_call_btn:{
                Call("0795004792");
                break;
            }
            case R.id.community16_map_btn:{
                LatLng latLng = new LatLng(32.28613763153244, 35.895139812819835);
                ShowInMap(latLng);
                break;
            }
            case R.id.community16_call_btn:{
                Call("0790046965");
                break;
            }
            case R.id.community17_map_btn:{
                LatLng latLng = new LatLng(31.187436689080737, 35.702932932058644);
                ShowInMap(latLng);
                break;
            }
            case R.id.community17_call_btn:{
                Call("0791600987");
                break;
            }
            case R.id.community18_map_btn:{
                LatLng latLng = new LatLng(30.834685591565044, 35.61305732484633);
                ShowInMap(latLng);
                break;
            }
            case R.id.community18_call_btn:{
                Call("0791545772");
                break;
            }
            case R.id.community19_map_btn:{
                LatLng latLng = new LatLng(30.20782806614486, 35.72987647653354);
                ShowInMap(latLng);
                break;
            }
            case R.id.community19_call_btn:{
                Call("0791545772");
                break;
            }
            case R.id.community20_map_btn:{
                LatLng latLng = new LatLng(29.521755673133644, 35.00643819505708);
                ShowInMap(latLng);
                break;
            }
            case R.id.community20_call_btn:{
                Call("0791545772");
                break;
            }
            case R.id.community21_map_btn:{
                LatLng latLng = new LatLng(32.30256814749728, 36.32718190695423);
                ShowInMap(latLng);
                break;
            }
            case R.id.community21_call_btn:{
                Call("0796027807");
                break;
            }
            case R.id.community22_map_btn:{
                LatLng latLng = new LatLng(31.905030432677226, 36.598463736022914);
                ShowInMap(latLng);
                break;
            }
            case R.id.community22_call_btn:{
                Call("0791047087");
                break;
            }
        }
    }

    public void ButtonInitialize(){
        mapBtn1 = view.findViewById(R.id.community1_map_btn);
        mapBtn1.setOnClickListener(this);
        mapBtn2 = view.findViewById(R.id.community2_map_btn);
        mapBtn2.setOnClickListener(this);
        mapBtn3 = view.findViewById(R.id.community3_map_btn);
        mapBtn3.setOnClickListener(this);
        mapBtn4 = view.findViewById(R.id.community4_map_btn);
        mapBtn4.setOnClickListener(this);
        mapBtn5 = view.findViewById(R.id.community5_map_btn);
        mapBtn5.setOnClickListener(this);
        mapBtn6 = view.findViewById(R.id.community6_map_btn);
        mapBtn6.setOnClickListener(this);
        mapBtn7 = view.findViewById(R.id.community7_map_btn);
        mapBtn7.setOnClickListener(this);
        mapBtn8 = view.findViewById(R.id.community8_map_btn);
        mapBtn8.setOnClickListener(this);
        mapBtn9 = view.findViewById(R.id.community9_map_btn);
        mapBtn9.setOnClickListener(this);
        mapBtn10 = view.findViewById(R.id.community10_map_btn);
        mapBtn10.setOnClickListener(this);
        mapBtn11 = view.findViewById(R.id.community11_map_btn);
        mapBtn11.setOnClickListener(this);
        mapBtn12 = view.findViewById(R.id.community12_map_btn);
        mapBtn12.setOnClickListener(this);
        mapBtn13 = view.findViewById(R.id.community13_map_btn);
        mapBtn13.setOnClickListener(this);
        mapBtn14 = view.findViewById(R.id.community14_map_btn);
        mapBtn14.setOnClickListener(this);
        mapBtn15 = view.findViewById(R.id.community15_map_btn);
        mapBtn15.setOnClickListener(this);
        mapBtn16 = view.findViewById(R.id.community16_map_btn);
        mapBtn16.setOnClickListener(this);
        mapBtn17 = view.findViewById(R.id.community17_map_btn);
        mapBtn17.setOnClickListener(this);
        mapBtn18 = view.findViewById(R.id.community18_map_btn);
        mapBtn18.setOnClickListener(this);
        mapBtn19 = view.findViewById(R.id.community19_map_btn);
        mapBtn19.setOnClickListener(this);
        mapBtn20 = view.findViewById(R.id.community20_map_btn);
        mapBtn20.setOnClickListener(this);
        mapBtn21 = view.findViewById(R.id.community21_map_btn);
        mapBtn21.setOnClickListener(this);
        mapBtn22 = view.findViewById(R.id.community22_map_btn);
        mapBtn22.setOnClickListener(this);

        callBtn1 = view.findViewById(R.id.community1_call_btn);
        callBtn1.setOnClickListener(this);
        callBtn2 = view.findViewById(R.id.community2_call_btn);
        callBtn2.setOnClickListener(this);
        callBtn3 = view.findViewById(R.id.community3_call_btn);
        callBtn3.setOnClickListener(this);
        callBtn4 = view.findViewById(R.id.community4_call_btn);
        callBtn4.setOnClickListener(this);
        callBtn5 = view.findViewById(R.id.community5_call_btn);
        callBtn5.setOnClickListener(this);
        callBtn6 = view.findViewById(R.id.community6_call_btn);
        callBtn6.setOnClickListener(this);
        callBtn7 = view.findViewById(R.id.community7_call_btn);
        callBtn7.setOnClickListener(this);
        callBtn8 = view.findViewById(R.id.community8_call_btn);
        callBtn8.setOnClickListener(this);
        callBtn9 = view.findViewById(R.id.community9_call_btn);
        callBtn9.setOnClickListener(this);
        callBtn10 = view.findViewById(R.id.community10_call_btn);
        callBtn10.setOnClickListener(this);
        callBtn11 = view.findViewById(R.id.community11_call_btn);
        callBtn11.setOnClickListener(this);
        callBtn12 = view.findViewById(R.id.community12_call_btn);
        callBtn12.setOnClickListener(this);
        callBtn13 = view.findViewById(R.id.community13_call_btn);
        callBtn13.setOnClickListener(this);
        callBtn14 = view.findViewById(R.id.community14_call_btn);
        callBtn14.setOnClickListener(this);
        callBtn15 = view.findViewById(R.id.community15_call_btn);
        callBtn15.setOnClickListener(this);
        callBtn16 = view.findViewById(R.id.community16_call_btn);
        callBtn16.setOnClickListener(this);
        callBtn17 = view.findViewById(R.id.community17_call_btn);
        callBtn17.setOnClickListener(this);
        callBtn18 = view.findViewById(R.id.community18_call_btn);
        callBtn18.setOnClickListener(this);
        callBtn19 = view.findViewById(R.id.community19_call_btn);
        callBtn19.setOnClickListener(this);
        callBtn20 = view.findViewById(R.id.community20_call_btn);
        callBtn20.setOnClickListener(this);
        callBtn21 = view.findViewById(R.id.community21_call_btn);
        callBtn21.setOnClickListener(this);
        callBtn22 = view.findViewById(R.id.community22_call_btn);
        callBtn22.setOnClickListener(this);
    }

    public void Call(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phone)));
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
}