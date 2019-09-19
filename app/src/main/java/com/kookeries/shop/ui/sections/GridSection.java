package com.kookeries.shop.ui.sections;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.ui.adapters.ProductGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSection {
    private Activity activity;
    private View gridWrapper;
    private SectionStateObserver stateObserver;
    private ProductGridAdapter.OnItemClickListener itemClickListener;

    private ProductGridAdapter gridAdapter;
    private List<Product> products;
    private String title = null;
    private String emptyMessage = null;
    private String reloadMessage = null;
    private String refreshMessage = null;

    private int gridColumnCount;

    private LinearLayout titleWrapper, reloadWrapper, emptyWrapper;
    private TextView tvTitle;
    private RecyclerView rvGridItems;

    public GridSection(Activity activity, View section,
                       int gridColumnCount,
                       SectionStateObserver sectionStateObserver,
                       ProductGridAdapter.OnItemClickListener itemClickListener) {
        this.activity = activity;
        this.gridWrapper = section;
        this.gridColumnCount = gridColumnCount;
        this.stateObserver = sectionStateObserver;
        this.itemClickListener = itemClickListener;

        this.initSection();
    }

    private void initSection() {
        products = new ArrayList<>();

        gridAdapter = new ProductGridAdapter(activity, products, itemClickListener);
        gridAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                if (gridAdapter.getItemCount() > 0) {
                    stateLoaded();
                } else {
                    stateEmpty(null);
                }

            }
        });

        rvGridItems = (RecyclerView) gridWrapper.findViewById(R.id.sectionBody);
        rvGridItems.setLayoutManager(new GridLayoutManager(activity, gridColumnCount));
        rvGridItems.setAdapter(gridAdapter);

        titleWrapper = gridWrapper.findViewById(R.id.sectionTitleWrapper);
        titleWrapper.setVisibility(View.GONE);
        tvTitle = titleWrapper.findViewById(R.id.sectionTitle);

        reloadWrapper = gridWrapper.findViewById(R.id.sectionReload);
        emptyWrapper = gridWrapper.findViewById(R.id.sectionEmpty);

        this.stateReadyToReload();
    }

    /*
    ------------------------------------
    |   Accessors & Mutators
    ------------------------------------
    */

    public View getGridWrapper() {
        return gridWrapper;
    }

    public SectionStateObserver getStateObserver() {
        return stateObserver;
    }

    public void setStateObserver(SectionStateObserver stateObserver) {
        this.stateObserver = stateObserver;
    }

    public ProductGridAdapter.OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ProductGridAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductGridAdapter getGridAdapter() {
        return gridAdapter;
    }

    public void setGridAdapter(ProductGridAdapter gridAdapter) {
        this.gridAdapter = gridAdapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (this.title != null) {
            titleWrapper.setVisibility(View.VISIBLE);
            tvTitle.setText(this.title);
        } else {
            titleWrapper.setVisibility(View.VISIBLE);
        }
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public String getReloadMessage() {
        return reloadMessage;
    }

    public void setReloadMessage(String reloadMessage) {
        this.reloadMessage = reloadMessage;
    }

    public String getRefreshMessage() {
        return refreshMessage;
    }

    public void setRefreshMessage(String refreshMessage) {
        this.refreshMessage = refreshMessage;
    }

    /*
    ------------------------------------
    |   Section States
    ------------------------------------
    */

    private void stateReadyToReload() {
        reloadWrapper.setVisibility(View.VISIBLE);
        emptyWrapper.setVisibility(View.GONE);
        rvGridItems.setVisibility(View.GONE);

        this.stateObserver.sectionReadyToReload(this.gridAdapter);
    }

    private void stateEmpty(String message) {
        reloadWrapper.setVisibility(View.GONE);
        emptyWrapper.setVisibility(View.VISIBLE);
        rvGridItems.setVisibility(View.GONE);

        if (message != null) {
            ((TextView) emptyWrapper.findViewById(R.id.message)).setText(message);
        }

        this.stateObserver.sectionEmpty();
    }

    private void stateLoaded() {
        reloadWrapper.setVisibility(View.GONE);
        emptyWrapper.setVisibility(View.GONE);
        rvGridItems.setVisibility(View.VISIBLE);

        this.stateObserver.sectionLoaded();
    }

    /*
    ------------------------------------
    |   Section State Observer
    ------------------------------------
    */

    public interface SectionStateObserver {
        void sectionReadyToReload(ProductGridAdapter adapter);

        void sectionEmpty();

        void sectionLoaded();
    }

}