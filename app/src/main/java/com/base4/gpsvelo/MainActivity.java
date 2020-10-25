package com.base4.gpsvelo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private double globalLat1 = 0.0;
    private double globalLon1 = 0.0;
    private int startSeconds = 0;
    private Context context;
    //final private MyBluetoothService mbs = new MyBluetoothService();
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        startSeconds = (int) System.currentTimeMillis();
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
        }//if
        else {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    int currentTimeMillis = (int) System.currentTimeMillis();
                    double currentLat = location.getLatitude();
                    double currentLon = location.getLongitude();
                    int velocity = (int)((getDistance(globalLat1, globalLon1, currentLat, currentLon) * 60.0 * 60.0) / ((double)(currentTimeMillis - startSeconds) / 1000.0));
                    //mbs.write(getBytes(velocity));
                    setNum(velocity);
                    startSeconds = currentTimeMillis;
                    globalLat1 = currentLat;
                    globalLon1 = currentLon;
                }//onLocationChanged

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }//onProviderDisabled

                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                }//onProviderEnabled

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO Auto-generated method stub
                }//onStatusChanged

            });

        }//else

        /*BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());

        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:14:03:05:FF:48");
        System.out.println(hc05.getName());

        BluetoothSocket btSocket = null;
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }//catch
            counter++;
        } while (!btSocket.isConnected() && counter < 3);

        try{
            write(btSocket.getOutputStream(), 65);
            cancel(btSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }//catch
*/
    }//onCreate

    public void write(OutputStream os, int velocity){
        try {
            os.write(53);
            //os.write(getBytes(velocity));
        } catch (IOException e) {
            e.printStackTrace();
        }//catch

    }//write

    public void cancel(OutputStream os){
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//catch
    }//cancel

    public byte[] getBytes(int n){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(n);
        return b.array();
    }//getBytes

    public void setNum(int n){
        ImageView ones = findViewById(R.id.ones);
        ImageView tens = findViewById(R.id.tens);
        int oneNum = getOnes(n);
        int tenNum = getTens(n);
        ones.setBackground(getDraw(oneNum));
        tens.setBackground(getDraw(tenNum));
        if(getTens(n) == 0) tens.setVisibility(View.INVISIBLE);
        else tens.setVisibility(View.VISIBLE);

    }//setNum

    public int getOnes(int n){
        return n % 10;
    }//getOnes

    public int getTens(int n){
        return n / 10;
    }//getTens

    public Drawable getDraw(int n){
        if(n == 1) return getDrawable(R.drawable.one);
        else if(n == 2) return getDrawable(R.drawable.two);
        else if(n == 3) return getDrawable(R.drawable.three);
        else if(n == 4) return getDrawable(R.drawable.four);
        else if(n == 5) return getDrawable(R.drawable.five);
        else if(n == 6) return getDrawable(R.drawable.six);
        else if(n == 7) return getDrawable(R.drawable.seven);
        else if(n == 8) return getDrawable(R.drawable.eight);
        else if(n == 9) return getDrawable(R.drawable.nine);
        return getDrawable(R.drawable.zero);
    }//getStringRep

    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        final double R = 6371000.0;
        final double mu1 = lat1 * Math.PI / 180.0;
        final double mu2 = lat2 * Math.PI / 180.0;
        final double deltaMu = (lat2 - lat1) * Math.PI / 180.0;
        final double deltaLambda = (lon2 - lon1) * Math.PI / 180.0;

        final double a = Math.sin(deltaMu / 2.0) * Math.sin(deltaMu / 2.0) + Math.cos(mu1) * Math.cos(mu2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        final double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        final double d = R * c;
        final double dMile = d * 0.00062137119;
        return dMile;
    }//getDistance



}//MainActivity