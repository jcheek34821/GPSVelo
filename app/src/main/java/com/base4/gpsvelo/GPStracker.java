package com.base4.gpsvelo;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class GPStracker implements LocationListener {
    Context context;
    public GPStracker(Context c){
        context = c;
    }//GPStracker

    public Location getLocation(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
            return null;
        }//if
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        }//if
        else{
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
        }//else
        return null;
    }//getLocation

    @Override
    public void onProviderDisabled(String provider) {

    }//onProviderDisabled

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }//onStatusChanged

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
    }//onLocationChanged

    @Override
    public void onProviderEnabled(String provider) {

    }//onProviderEnabled

}//GPStracker
