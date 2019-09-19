package com.kookeries.shop.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.activities.CatalogActivity;
import com.kookeries.shop.ui.adapters.CategoriesAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.ui.adapters.GenericListAdapter;
import com.kookeries.shop.ui.sections.ListSection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CategoriesFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View fragmentRootView;

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
        fragmentRootView = inflater.inflate(R.layout.fragment_categories, container, false);

        initSwipeRefreshLayout();
        populateData();

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

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentRootView.findViewById(R.id.refreshLayout);
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
        CategoriesAdapter.OnItemClickListener onItemClickListener = new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                Category.seleted = category;
                getActivity().startActivity(new Intent(getActivity(), CatalogActivity.class));
            }
        };

        ListSection categoryListSection = new ListSection(fragmentRootView.findViewById(R.id.sectionCategories),
                new CategoriesAdapter(getActivity(), new ArrayList<Category>(), R.layout.card_catalog_horizontal, onItemClickListener),
                new ListSection.SectionStateObserver<Category>() {
                    @Override
                    public void sectionReadyToReload(final GenericListAdapter<Category> adapter) {
                        API.getCategories(new ApiResponse.CatalogListener<Category>() {
                            @Override
                            public void onSuccess(List<Category> data) {
                                adapter.setData(data);
                                if (mSwipeRefreshLayout.isRefreshing())
                                    mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setEnabled(false);
                            }

                            @Override
                            public void onFailure(JSONObject response) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                                if (mSwipeRefreshLayout.isRefreshing())
                                    mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onException(Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                if (mSwipeRefreshLayout.isRefreshing())
                                    mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void sectionEmpty() {

                    }

                    @Override
                    public void sectionLoaded() {

                    }
                }
        );
        categoryListSection.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        categoryListSection.setDividerHeight(32);
    }
}
