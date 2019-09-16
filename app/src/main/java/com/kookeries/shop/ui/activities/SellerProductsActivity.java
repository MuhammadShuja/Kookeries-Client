package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.ProductListAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;
import com.kookeries.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SellerProductsActivity extends AppCompatActivity {
    private static final String TAG = "SellerProductsActivity";

//    private PlacesClient placesClient;
//    private FindCurrentPlaceRequest currentPlaceRequest;

    private ListView lvProducts;
    private List<Product> products = new ArrayList<>();
    private ProductListAdapter productListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_seller_products);
        API.instantiate(getApplicationContext());

        setupToolbar();
        setupProducts();
        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_products_menu, menu);
        Utils.menuIconColor(menu.findItem(R.id.add), Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add) {
            startActivity(new Intent(SellerProductsActivity.this, SellerProductNewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Products");
    }

    private void setupProducts() {
        lvProducts = (ListView) findViewById(R.id.lvProducts);
        productListAdapter = new ProductListAdapter(this, products, R.layout.card_product_horizontal);
        lvProducts.setAdapter(productListAdapter);
    }

    private void populateData() {
        User.getProducts(new User.ProductsReadyListener() {
            @Override
            public void onReady(List<Product> data) {
                products.clear();
//                products.addAll(data);

                for (int i = 0; i < 15; i++) {
                    products.add(data.get(0));
                }
                Log.d(TAG, API.PRELOG_CHECK + products.size());

                productListAdapter.setProducts(data);
            }
        });
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