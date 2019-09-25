package com.example.android.gymple;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.internal.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mw on 07-09-2016.
 */
public class GeocodeAddressIntentService extends IntentService {

    private static final String TAG = GeocodeAddressIntentService.class.getSimpleName();

    protected ResultReceiver resultReceiver;


    double latitude, longitude;
    List<Address> addresses = null;

    public GeocodeAddressIntentService() {
        super("AddressService");
    }

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;

        public static final int USE_ADDRESS_NAME = 1;
        public static final int USE_ADDRESS_LOCATION = 2;

        public static final String PACKAGE_NAME =
                "com.rutvik.trails";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
        public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
        public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
        public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
        public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        int fetchType = intent.getIntExtra(Constants.FETCH_TYPE_EXTRA, 0);
        Log.e(TAG, "fetchType == " + fetchType);

        if (fetchType == Constants.USE_ADDRESS_NAME) {
            String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);
            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }
        } else if (fetchType == Constants.USE_ADDRESS_LOCATION) {
            latitude = intent.getDoubleExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, 0);
            longitude = intent.getDoubleExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, 0);

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + latitude + ", Longitude = " +
                        longitude, illegalArgumentException);
            }
        } else {
            errorMessage = "Unknown Type";
            Log.e(TAG, errorMessage);
        }

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Not Found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
        } else {

            for (Address address : addresses) {
                String outputAddress = "";
                 for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                 if(address.getMaxAddressLineIndex()==2){
                 break;
                 }
                 outputAddress += " --- " + address.getAddressLine(i);
                 }
                 Log.e(TAG, outputAddress);
            }

            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            for (int i = address.getMaxAddressLineIndex()-1; i >= 0; i--) {
                if(i==1){
                    break;
                }
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "Address Found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments), address);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }

    public void GetAddress()
    {
        if(addresses == null)
        {
            Log.d("address null", "address is null");
        }
        else
        {
            String addrSize = "addr size = " + addresses.size();
            Log.d("address size", addrSize);
        }
    }
}