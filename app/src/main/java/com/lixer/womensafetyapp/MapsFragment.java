package com.lixer.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapsFragment extends Fragment {

    private GoogleMap googleMap;
    private ProgressDialog progressDialog;
    String institute_id="";
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap gM) {

            Cursor cursor = new user_db(rootV.getContext()).viewMethod();
            cursor.moveToNext();
            institute_id = String.valueOf(cursor.getInt(0));

            googleMap =gM;
            progressDialog = ProgressDialog.show(rootV.getContext(), "",
                    "Loading Please Wait....", true);
            httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/safe_locations/"+institute_id);
            h2.execute();





        }

    };

    public class httpBackgroundAsync extends AsyncTask<Void,Void,String> {

        String url;
        String json_string;



        public httpBackgroundAsync(String url) {
            this.url = url;

        }

        public httpBackgroundAsync() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(Void... voids) {

            URL url2 = null;

            try{
                url2 = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = bufferedReader.readLine()) != null){
                    stringBuilder.append(json_string+"/n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        if(s != null){

            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){
                    progressDialog.dismiss();
                    JSONArray safe_location= jsonObject.getJSONArray("safe_location");
                    for (int i = 0 ; i<safe_location.length() ; i++){
                        JSONObject ob = safe_location.getJSONObject(i);
                        double lat= Double.parseDouble(ob.getString("latitude"));
                        double lng= Double.parseDouble(ob.getString("longitude"));
                        LatLng safe_point = new LatLng(lat,lng);

                        googleMap.addMarker(new MarkerOptions().position(safe_point).title("Marker in Safe Point"));
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(safe_point));

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(safe_point,15f));




                    }





                }else{
                    progressDialog.dismiss();
                    Toast.makeText(rootV.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else{
            progressDialog.dismiss();
            Toast.makeText(rootV.getContext(), "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
            httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/safe_locations/"+institute_id);
            h2.execute();
        }

        }



    }

    private View rootV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         rootV =inflater.inflate(R.layout.fragment_maps, container, false);

        return rootV;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}