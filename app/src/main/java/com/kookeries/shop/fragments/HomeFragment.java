package com.kookeries.shop.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.activities.ProductActivity;
import com.kookeries.shop.activities.SellerDashboardActivity;
import com.kookeries.shop.adapters.ProductGridAdapter;
import com.kookeries.shop.adapters.SliderAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.Slide;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private List<Slide> slides = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private ProductGridAdapter productGridAdapter;

    private int HEADER_FLAG = 0;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final int VECTOR_HEADER = 0;
    private static final int GRADIENT_HEADER = 1;
    private FrameLayout headerLayout;
    private ViewGroup.LayoutParams headerLayoutParams;
    private static int VECTOR_HEADER_HEIGHT, GRADIENT_HEADER_HEIGHT;
    private NestedScrollView homeScrollView;

    private View fragmentRootView;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRootView = inflater.inflate(R.layout.fragment_home, container, false);

        productGridAdapter = new ProductGridAdapter(getContext(), products, new ProductGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Product.selected = product;
                startActivity(new Intent(getActivity(), ProductActivity.class));
            }
        });
        populateData();
        initHeader();
        initSlider();

        RecyclerView rvProducts = (RecyclerView) fragmentRootView.findViewById(R.id.sectionBody);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.setAdapter(productGridAdapter);

        ((LinearLayout) fragmentRootView.findViewById(R.id.btnSell)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SellerDashboardActivity.class));
            }
        });

        return fragmentRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initHeader(){
        VECTOR_HEADER_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());
        GRADIENT_HEADER_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 76, getResources().getDisplayMetrics());

        headerLayout = (FrameLayout) fragmentRootView.findViewById(R.id.header_with_vector_bg);
        headerLayoutParams = headerLayout.getLayoutParams();
        homeScrollView = (NestedScrollView) fragmentRootView.findViewById(R.id.homeScrollView);
        homeScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = homeScrollView.getScrollY(); // For ScrollView
                if(scrollY >= 220 && HEADER_FLAG == VECTOR_HEADER){
                    headerLayoutParams.height = GRADIENT_HEADER_HEIGHT;
                    headerLayout.setLayoutParams(headerLayoutParams);
                    headerLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.gradient_theme));
                    HEADER_FLAG = GRADIENT_HEADER;
                }
                else if(scrollY < 220 && HEADER_FLAG == GRADIENT_HEADER){
                    headerLayoutParams.height = VECTOR_HEADER_HEIGHT;
                    headerLayout.setLayoutParams(headerLayoutParams);
                    headerLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_slider_vector));
                    HEADER_FLAG = VECTOR_HEADER;
                }
            }
        });
    }

    private void initSlider() {
        mPager = (ViewPager) fragmentRootView.findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(slides, headerLayout, getContext()));

        CirclePageIndicator indicator = (CirclePageIndicator)
                fragmentRootView.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES = slides.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                if(HEADER_FLAG == VECTOR_HEADER){
                    headerLayout.getBackground().setTint(Color.parseColor(slides.get(currentPage).getColor()));
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

    private void populateData(){
        slides.add(new Slide("b0", "#FFC502", null));
        slides.add(new Slide("b1", "#5BA700", null));
        slides.add(new Slide("b2", "#01313F", null));
        slides.add(new Slide("b3", "#278EB7", null));
        slides.add(new Slide("b4", "#00142C", null));
        slides.add(new Slide("b5", "#317CEF", null));
        slides.add(new Slide("b6", "#532ED4", null));
        slides.add(new Slide("b7", "#1C1B17", null));

        API.getProducts(null, new ApiResponse.CatalogListener<Product>() {
            @Override
            public void onSuccess(List<Product> data) {
                productGridAdapter.setData(data);
            }

            @Override
            public void onFailure(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
