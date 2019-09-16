package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.ui.adapters.ProductImagesAdapter;
import com.kookeries.shop.ui.adapters.ProductGridAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Cart;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.ui.sections.GridSection;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridSection mGridSection;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static int CART_COUNT = Cart.count();

    private TextView name, price, oldPrice, discount, category, description, tvAddToCart, tvActionCartCount;
    private LinearLayout categoryWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_product);

        setupToolbar();
        initSwipeRefreshLayout();
        initSlider();

        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        oldPrice = (TextView) findViewById(R.id.oldPrice);
        discount = (TextView) findViewById(R.id.discount);
        category = (TextView) findViewById(R.id.category);
        description = (TextView) findViewById(R.id.description);

        categoryWrapper = (LinearLayout) findViewById(R.id.categoryWrapper);

        if(Product.selected != null){
            Log.d(TAG, API.PRELOG_CHECK + Product.selected.getName());
            name.setText(Product.selected.getName());
            price.setText(Product.selected.getPrice());
            if(Product.selected.getOldPrice() != null){
                ((LinearLayout) findViewById(R.id.oldPriceWrapper)).setVisibility(View.VISIBLE);
                oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                oldPrice.setText(Product.selected.getOldPrice());
                discount.setText(Product.selected.getDiscount());
            }
            if(Product.selected.getCategory() == null){
                categoryWrapper.setVisibility(View.GONE);
            }
            else{
                setupProducts();
                category.setText(Product.selected.getCategory().getName());
                categoryWrapper.setVisibility(View.VISIBLE);
                categoryWrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Category.seleted = Product.selected.getCategory();
                        startActivity(new Intent(ProductActivity.this, CatalogActivity.class));
                    }
                });
            }
            description.setText(Product.selected.getDescription());
        }

        setupAddToCart();
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
            startActivity(new Intent(ProductActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSlider() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ProductImagesAdapter(Product.selected.getImages(), this));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES = Product.selected.getImages().size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.menu_item_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setTitle(Product.selected.getName());
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

    private void setupProducts(){
        mGridSection = new GridSection(ProductActivity.this, findViewById(R.id.sectionGrid),
                new GridSection.SectionStateObserver() {
                    @Override
                    public void sectionReadyToReload(final ProductGridAdapter adapter) {
                        Product.selected.getCategory().takeProducts(false, 8, true, Product.selected, new Category.DataReadyListener() {
                            @Override
                            public void onReady(List<Product> productsList) {
                                adapter.setData(productsList);
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
                        startActivity(new Intent(ProductActivity.this, ProductActivity.class));
                    }
                }
        );
        mGridSection.setTitle("You may also like");
    }

    private void setupAddToCart(){
        tvAddToCart = (TextView) findViewById(R.id.tvAddToCart);

        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.addItem(Product.selected);
                Toast.makeText(getBaseContext(), "Item added to cart", Toast.LENGTH_LONG).show();
                CART_COUNT++;
                setupCartBadge();
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
        Product.selected.getCategory().takeProducts(true, 8, true, Product.selected, new Category.DataReadyListener() {
            @Override
            public void onReady(List<Product> productsList) {
                mGridSection.getGridAdapter().setData(productsList);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}