package com.kookeries.shop.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.activities.CatalogActivity;
import com.kookeries.shop.ui.adapters.CategoriesAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.models.Category;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Category> categories = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;

    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        initSwipeRefreshLayout(view);

        categoriesAdapter = new CategoriesAdapter(getContext(), categories, R.layout.card_catalog_horizontal);
        populateData();

        ListView lvCatalog = (ListView) view.findViewById(R.id.lvCatalog);
        lvCatalog.setAdapter(categoriesAdapter);

        lvCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category.seleted = categories.get(i);
                getActivity().startActivity(new Intent(getActivity(), CatalogActivity.class));
            }
        });

        return view;
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

    private void initSwipeRefreshLayout(View view){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        int indicatorColorArray[] = {R.color.primaryColor, R.color.orange};
        mSwipeRefreshLayout.setColorSchemeResources(indicatorColorArray);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateData();
            }
        });
    }

    private void populateData(){
        API.getCategories(new ApiResponse.CatalogListener<Category>() {
            @Override
            public void onSuccess(List<Category> data) {
                categories.clear();
                categories.addAll(data);
                categoriesAdapter.notifyDataSetChanged();
                if(mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                if(mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onException(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                if(mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
