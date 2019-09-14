package com.kookeries.shop.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.adapters.ProductGridAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Cart;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private List<Product> products = new ArrayList<>();
    private ProductGridAdapter productGridAdapter;

    private static int CART_COUNT = Cart.count();
    private TextView tvActionCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_catalog);
        API.instantiate(getApplicationContext());

        setupToolbar();
        setupProducts();
        populateData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CART_COUNT = Cart.count();
        setupCartBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        tvActionCartCount = (TextView) actionView.findViewById(R.id.actionCartCount);

        setupCartBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        else if (id == R.id.search) {
            return true;
        }
        else if (id == R.id.cart) {
            startActivity(new Intent(CatalogActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.menu_item_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(Category.seleted != null){
            setTitle(Category.seleted.getName());

            ImageView cover = (ImageView) findViewById(R.id.catalogCover);
            Glide
                    .with(this)
                    .load(Category.seleted.getThumbnail())
                    .into(cover);
        }
    }

    private void setupProducts(){
        productGridAdapter = new ProductGridAdapter(this, products, new ProductGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Product.selected = product;
                startActivity(new Intent(CatalogActivity.this, ProductActivity.class));
            }
        });

        RecyclerView rvProducts = (RecyclerView) findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productGridAdapter);
        rvProducts.setNestedScrollingEnabled(false);
    }

    private void populateData(){
        Category.seleted.getProducts(new Category.DataReadyListener() {
            @Override
            public void onReady(List<Product> productsList) {
                products = productsList;
                productGridAdapter.setData(productsList);
            }
        });
    }

    private void setupCartBadge(){
        if (tvActionCartCount != null) {
            if (CART_COUNT == 0) {
                if (tvActionCartCount.getVisibility() != View.GONE) {
                    tvActionCartCount.setVisibility(View.GONE);
                }
            } else {
                if(CART_COUNT > 99){
                    tvActionCartCount.setText(String.valueOf("99+"));
                }
                else{
                    tvActionCartCount.setText(String.valueOf(CART_COUNT));
                }
                if (tvActionCartCount.getVisibility() != View.VISIBLE) {
                    tvActionCartCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
