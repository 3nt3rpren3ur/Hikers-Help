package com.likhith.hikershelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.Format;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null)
            {
                updateLocationInfo(location);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && (grantResults[0]==PackageManager.PERMISSION_GRANTED || grantResults[1]==PackageManager.PERMISSION_GRANTED))
        {
            startListening();
        }
    }

    public void startListening()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        }
    }
    public void updateLocationInfo(Location location)
    {
        TextView latitude=findViewById(R.id.latitude);
        TextView longitude=findViewById(R.id.longitude);
        TextView altitude=findViewById(R.id.altitude);
        TextView accuracy=findViewById(R.id.accuracy);
        TextView address=findViewById(R.id.address);


        latitude.setText("Latitude: "+Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
        longitude.setText("Longitude: "+Location.convert(location.getLongitude(), Location.FORMAT_DEGREES));
        altitude.setText("Altitude: "+Location.convert(location.getAltitude(), Location.FORMAT_DEGREES));
        accuracy.setText("Accuracy: "+Location.convert(location.getAccuracy(), Location.FORMAT_DEGREES));

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        try
        {
            String add="could not find location";
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses!=null && addresses.size()>0)
            {
                Log.i("addresses",addresses.get(0).toString());
                add="Address: \n";
                if(addresses.get(0).getSubThoroughfare()!=null)
                {
                    add+=addresses.get(0).getSubThoroughfare()+", ";
                }
                if(addresses.get(0).getThoroughfare()!=null)
                {
                    add+=addresses.get(0).getThoroughfare()+"\n";
                }
                if(addresses.get(0).getLocality()!=null)
                {
                    add+=addresses.get(0).getLocality()+"\n";
                }
                if(addresses.get(0).getPostalCode()!=null)
                {
                    add+=addresses.get(0).getPostalCode()+"\n";
                }
                if(addresses.get(0).getCountryName()!=null)
                {
                    add+=addresses.get(0).getCountryName()+"\n";
                }
                address.setText(add);
            }
            else
            {
                address.setText(add);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
