package shaked.aviad.aviv.myfinalconverter.view_fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import it.sephiroth.android.library.tooltip.Tooltip;
import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.internet.VolleyReq;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.ExchangePlaceObject;
import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadMapInformationListener;

import static shaked.aviad.aviv.myfinalconverter.controller_activity.MainActivity.getColor;


public class FragmentMap extends Fragment implements LocationListener, OnMapReadyCallback, DownloadMapInformationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private VolleyReq volleyReq;
    private final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;
    private SupportMapFragment map;
    private boolean firstEnter = true;
    private Location lastLocation;
    private boolean isMarkersPlaced = false;
    private boolean isMapReady = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleyReq = new VolleyReq(getActivity(), this);
        Log.d("onCreate", " ddd");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView", " ddd");
        final View view = inflater.inflate(R.layout.activity_maps, container, false);

        Snackbar snackbar = Snackbar
                .make(view, "Exchange Money places near you", Snackbar.LENGTH_INDEFINITE);
        View newView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) newView.getLayoutParams();
        params.gravity = Gravity.TOP;
        newView.setLayoutParams(params);
        snackbar.show();
        map = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        view.findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkGps()) {
                    initMapPosition();
                }else{
                    buildAlertMessageNoGps();
                }
            }
        });
        return view;
    }

    private boolean checkGps() {
        Log.d("checkGps", " ddd");
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMapReady", " ddd");
        if (firstEnter) {
            initToolTip();
            firstEnter = false;
        }
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            buildGoogleApiClient();
            googleApiClient.connect();
        }

    }

    private void initToolTip() {
        Log.d("initToolTip", " ddd");
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = display.getWidth();
        Tooltip.make(getActivity(),
                new Tooltip.Builder(101)
                        .anchor(size, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 10000000)
                        .activateDelay(400)
                        .showDelay(300)
                        .text("Check where is the nearest Exchange place!")
                        .maxWidth(width)
                        .withArrow(false)
                        .withOverlay(true)
                        .floatingAnimation(Tooltip.AnimationBuilder.SLOW)
                        .build()
        ).show();
    }

    @Override
    public void onDoneDownload(ArrayList<ExchangePlaceObject> listPlaces) {
        isMarkersPlaced = true;
        Log.d("onDoneDownload", " ddd");
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.d("getInfoContents", " ddd");
                LinearLayout info = new LinearLayout(getContext());
                info.setBackgroundColor(getColor(getContext(), R.color.colorPrimaryDark));
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(getColor(getContext(), R.color.colorGreenIcon));

                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.ITALIC);
                title.setText(marker.getTitle());

       /* TextView snippet = new TextView(getContext());
        snippet.setTextColor(Color.GRAY);
        snippet.setText(marker.getSnippet());*/

                info.addView(title);
                // info.addView(snippet);

                return info;
            }
        });

        MarkerOptions markerOptions = new MarkerOptions();
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("marker", "drawable", getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 75,104, false);
        for (int i = 0; i < listPlaces.size(); i++) {
            String placeInfo = " " + listPlaces.get(i).getPlaceName().toUpperCase() + " \n " + listPlaces.get(i).getPlaceAddress() + " ";
            markerOptions.position(listPlaces.get(i).getPlaceLatLng());
            markerOptions.title(placeInfo);
           // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
            mMap.addMarker(markerOptions);
            Log.d("place name: ", listPlaces.get(i).getPlaceName());

        }


    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("onConnected", " ddd");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            if (checkGps()) {
                isMapReady = true;
                initMapPosition();
            } else {
                buildAlertMessageNoGps();
            }
        }
    }

    private void initMapPosition() {
        Log.d("initMapPosition", " ddd");

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.clear();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            Log.d("last location ", lastLocation.getLatitude() + "," + lastLocation.getLongitude());
            LatLng myLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            volleyReq.listPlacesCreate(myLocation.latitude, myLocation.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", " ddd");

        if (googleApiClient != null) {
            Log.d("googleApiClient", "ddd");
            googleApiClient.connect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop", "ddd");
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkGps() && isMapReady){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("readyToinit", "ddd");
                    initMapPosition();
                }
            },1800);

        }
        Log.d("gps false","ddd");
        Log.d("onResume",  " ddd");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("buildGoogleApiClient", " ddd");
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void initMap() {
        Log.d("initMap", " ddd");
        map.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", " ddd");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }
}
