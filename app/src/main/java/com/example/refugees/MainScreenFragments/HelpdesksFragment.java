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
import android.widget.Toast;

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

public class HelpdesksFragment extends Fragment implements View.OnClickListener {


    public HelpdesksFragment() {
        // Required empty public constructor
    }

    View view;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng userLocation;
    MaterialButton mapBtn1, mapBtn2, mapBtn3, mapBtn4, mapBtn5, mapBtn6, mapBtn7, mapBtn8, mapBtn9, mapBtn10, mapBtn11, mapBtn12, mapBtn13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_helpdesks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButtonInitialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.helpdesk1_map_btn:{
                LatLng latLng = new LatLng(31.877000032342707, 35.99576222382696);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk2_map_btn:{
                LatLng latLng = new LatLng(31.977831078366318, 35.92217319600955);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk3_map_btn:{
                LatLng latLng = new LatLng(32.088492727579826, 36.09816803755059);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk4_map_btn:{
                LatLng latLng = new LatLng(31.730189767617183, 35.793437238254064);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk5_map_btn:{
                LatLng latLng = new LatLng(32.06423636035091, 35.72267742356779);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk6_map_btn:{
                LatLng latLng = new LatLng(31.861958281819124, 35.64031232362207);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk7_map_btn:{
                LatLng latLng = new LatLng(30.218663689408487, 35.737831513196454);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk8_map_btn:{
                LatLng latLng = new LatLng(31.187296642252473, 35.70248920855095);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk9_map_btn:{
                LatLng latLng = new LatLng(29.520525516402298, 35.00794194886785);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk10_map_btn:{
                LatLng latLng = new LatLng(31.971651298113027, 35.881997921284565);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk11_map_btn:{
                LatLng latLng = new LatLng(32.32327688888242, 35.75154319530603);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk12_map_btn:{
                LatLng latLng = new LatLng(32.2758915210928, 35.89649291548452);
                ShowInMap(latLng);
                break;
            }
            case R.id.helpdesk13_map_btn:{
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
}