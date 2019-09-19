package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.ProductGridAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Cart;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.ui.sections.GridSection;

import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridSection mGridSection;

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
        initSwipeRefreshLayout();
        setupProducts();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        mGridSection = new GridSection(CatalogActivity.this, findViewById(R.id.sectionGrid), 2,
                new GridSection.SectionStateObserver() {
                    @Override
                    public void sectionReadyToReload(final ProductGridAdapter adapter) {
                        Category.seleted.getProducts(false, new Category.DataReadyListener() {
                            @Override
                            public void onReady(List<Product> productsList) {
                                adapter.setData(productsList);
                                mSwipeRefreshLayout.setEnabled(false);
                            }
                        });
                    }

                    @Override
                    public void sectionEmpty() {

                    }

                    @Override
                    public void sectionLoaded() {

                    }
                },
                new ProductGridAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product product) {
                        Product.selected = product;
                        startActivity(new Intent(CatalogActivity.this, ProductActivity.class));
                    }
                }
        );
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
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

    private void reloadData() {
        Category.seleted.getProducts(true, new Category.DataReadyListener() {
            @Override
            public void onReady(List<Product> productsList) {
                mGridSection.getGridAdapter().setData(productsList);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
