package com.kookeries.shop.ui.sections;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.models.OrderItem;
import com.kookeries.shop.ui.adapters.OrderListAdapter;
import com.kookeries.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class OrderListSection {
    private Activity activity;
    private View listWrapper;
    private int resource;
    private SectionStateObserver stateObserver;
    private OrderListAdapter.OnItemClickListener itemClickListener;

    private String actionText = null;
    private View.OnClickListener actionListener = null;

    private OrderListAdapter listAdapter;
    private List<OrderItem> items;
    private String title = null;
    private String emptyMessage = null;
    private String reloadMessage = null;
    private String refreshMessage = null;

    private int backgroundColor;
    private int titleSize = 14;

    private boolean sectionHeightBasedOnChildren = false;

    private LinearLayout titleWrapper, actionWrapper, reloadWrapper, emptyWrapper;
    private TextView tvTitle, tvAction;
    private ListView lvItems;

    public OrderListSection(Activity activity, View section, int resource,
                            SectionStateObserver sectionStateObserver,
                            OrderListAdapter.OnItemClickListener itemClickListener
    ) {
        this.activity = activity;
        this.listWrapper = section;
        this.resource = resource;
        this.stateObserver = sectionStateObserver;
        this.itemClickListener = itemClickListener;

        this.initSection();
    }

    private void initSection() {
        items = new ArrayList<>();

        listAdapter = new OrderListAdapter(activity, items, resource, itemClickListener);
        listAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            void checkEmpty() {
                if (listAdapter.getCount() > 0) {
                    stateLoaded();
                    if (sectionHeightBasedOnChildren)
                        Utils.setListViewHeightBasedOnChildren(lvItems);
                } else {
                    stateEmpty(null);
                }
            }
        });

        lvItems = (ListView) listWrapper.findViewById(R.id.sectionBody);
        lvItems.setAdapter(listAdapter);

        titleWrapper = listWrapper.findViewById(R.id.sectionTitleWrapper);
        titleWrapper.setVisibility(View.GONE);
        tvTitle = titleWrapper.findViewById(R.id.sectionTitle);

        actionWrapper = listWrapper.findViewById(R.id.sectionActionWrapper);
        actionWrapper.setVisibility(View.GONE);
        tvAction = actionWrapper.findViewById(R.id.sectionAction);

        reloadWrapper = listWrapper.findViewById(R.id.sectionReload);
        emptyWrapper = listWrapper.findViewById(R.id.sectionEmpty);

        this.stateReadyToReload();
    }

    /*
    ------------------------------------
    |   Accessors & Mutators
    ------------------------------------
    */

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public void setActionListener(View.OnClickListener actionListener) {
        this.actionListener = actionListener;
        if (this.actionListener != null) {
            actionWrapper.setVisibility(View.VISIBLE);
            tvAction.setOnClickListener(this.actionListener);
        } else {
            actionWrapper.setVisibility(View.GONE);
        }
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

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public void setReloadMessage(String reloadMessage) {
        this.reloadMessage = reloadMessage;
    }

    public void setRefreshMessage(String refreshMessage) {
        this.refreshMessage = refreshMessage;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        tvTitle.setTextSize(this.titleSize);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        listWrapper.setBackgroundColor(this.backgroundColor);
    }

    public void setSectionHeightBasedOnChildren(boolean sectionHeightBasedOnChildren) {
        this.sectionHeightBasedOnChildren = sectionHeightBasedOnChildren;
        if (sectionHeightBasedOnChildren) {
            Utils.setListViewHeightBasedOnChildren(this.lvItems);
        }
    }

    /*
    ------------------------------------
    |   Section States
    ------------------------------------
    */

    private void stateReadyToReload() {
        reloadWrapper.setVisibility(View.VISIBLE);
        emptyWrapper.setVisibility(View.GONE);
        lvItems.setVisibility(View.GONE);

        this.stateObserver.sectionReadyToReload(this.listAdapter);
    }

    private void stateEmpty(String message) {
        reloadWrapper.setVisibility(View.GONE);
        emptyWrapper.setVisibility(View.VISIBLE);
        lvItems.setVisibility(View.GONE);

        if (message != null) {
            ((TextView) emptyWrapper.findViewById(R.id.message)).setText(message);
        }

        this.stateObserver.sectionEmpty();
    }

    private void stateLoaded() {
        reloadWrapper.setVisibility(View.GONE);
        emptyWrapper.setVisibility(View.GONE);
        lvItems.setVisibility(View.VISIBLE);

        this.stateObserver.sectionLoaded();
    }

    /*
    ------------------------------------
    |   Section State Observer
    ------------------------------------
    */

    public interface SectionStateObserver {
        void sectionReadyToReload(OrderListAdapter adapter);

        void sectionEmpty();

        void sectionLoaded();
    }

}