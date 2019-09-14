package com.kookeries.shop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;

public class SellerProductsActivity extends AppCompatActivity {

//    private PlacesClient placesClient;
//    private FindCurrentPlaceRequest currentPlaceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);
        API.instantiate(getApplicationContext());
    }

//        if (!Places.isInitialized()) {
//            String gApiKey = getString(R.string.google_places_api_key);
//            Places.initialize(this, gApiKey);
//        }
//        placesClient = Places.createClient(this);

//        List<Place.Field> fields = new ArrayList<>();
//        fields.add(Place.Field.NAME);
//        currentPlaceRequest =
//                FindCurrentPlaceRequest.newInstance(fields);

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(currentPlaceRequest);
//            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
//                @Override
//                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
//
//                    if (task.isSuccessful()){
//                        FindCurrentPlaceResponse response = task.getResult();
//                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i("SerllerProductsActivity", String.format("Place '%s' has likelihood: %f",
//                                    placeLikelihood.getPlace().getName(),
//                                    placeLikelihood.getLikelihood()));
//                        }
//                    } else {
//                        Exception exception = task.getException();
//                        if (exception instanceof ApiException) {
//                            ApiException apiException = (ApiException) exception;
//                            Log.e("SellerProductsActivity", "Place not found: " + apiException.getStatusCode());
//                        }
//                    }
//
//                }
//            });
//        } else {
//            // A local method to request required permissions;
//            // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission();
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//        if(requestCode == 1){
//            loadPlaces();
//        }
//    }
//
//    private void getLocationPermission(){
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
//    }
//
//    public void loadPlaces(){
//    }
}
