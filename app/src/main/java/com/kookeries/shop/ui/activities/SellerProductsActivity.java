package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.GenericListAdapter;
import com.kookeries.shop.ui.adapters.ProductListAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;
import com.kookeries.shop.ui.sections.ListSection;
import com.kookeries.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SellerProductsActivity extends AppCompatActivity {
    private static final String TAG = "SellerProductsActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProductListAdapter.OnItemClickListener listener = null;
    private ListSection<Product> productListSection = null;

//    private PlacesClient placesClient;
//    private FindCurrentPlaceRequest currentPlaceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_seller_products);
        API.instantiate(getApplicationContext());

        setupToolbar();
        initSwipeRefreshLayout();
        setupProducts();
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

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.add) {
            startActivity(new Intent(SellerProductsActivity.this, SellerProductNewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        int indicatorColorArray[] = {R.color.primaryColor, R.color.orange};
        mSwipeRefreshLayout.setColorSchemeResources(indicatorColorArray);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
    }

    private void setupProducts() {
        if (listener == null) {
            listener = new ProductListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product product) {
                    Product.selected = product;
                    startActivity(new Intent(SellerProductsActivity.this, ProductActivity.class));
                }
            };
        }

        if (productListSection == null) {
            productListSection = new ListSection<>(findViewById(R.id.sectionListProducts),
                    new ProductListAdapter(this, new ArrayList<Product>(), R.layout.card_product_horizontal, listener),
                    new ListSection.SectionStateObserver<Product>() {
                        @Override
                        public void sectionReadyToReload(final GenericListAdapter<Product> adapter) {
                            User.getProducts(new User.ProductsReadyListener() {
                                @Override
                                public void onReady(List<Product> data) {
                                    adapter.setData(data);
                                }
                            });
                        }

                        @Override
                        public void sectionEmpty() {

                        }

                        @Override
                        public void sectionLoaded() {

                        }
                    });
            productListSection.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void reloadData() {
        User.getProducts(new User.ProductsReadyListener() {
            @Override
            public void onReady(List<Product> data) {
                productListSection.setData(data);

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
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