package com.kookeries.shop.ui.sections;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.ui.adapters.GenericListAdapter;
import com.kookeries.shop.utils.Utils;

import java.util.List;

public class ListSection<T> {
    private static final String TAG = "ListSection";

    public static final int STATE_READY_TO_RELOAD = 111;
    public static final int STATE_LOADED = 112;
    public static final int STATE_EMPTY = 113;

    private View container;
    private SectionStateObserver stateObserver;

    private String actionText = null;
    private View.OnClickListener actionListener = null;

    private GenericListAdapter adapter;

    private String title = null;
    private String emptyMessage = null;
    private String reloadMessage = null;
    private String refreshMessage = null;

    private int backgroundColor;
    private int titleSize = 14;
    private int dividerHeight;

    private boolean sectionHeightBasedOnChildren = false;

    private LinearLayout titleWrapper, actionWrapper, reloadWrapper, emptyWrapper;
    private TextView tvTitle, tvAction;
    private ListView lvItems;

    /*
    ------------------------------------
    |   Init Section
    ------------------------------------
    */

    public ListSection(View container,
                       GenericListAdapter adapter,
                       SectionStateObserver sectionStateObserver
    ) {
        this.container = container;
        this.adapter = adapter;
        this.stateObserver = sectionStateObserver;

        this.initSection();
    }

    private void initSection() {

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            void checkEmpty() {
                if (adapter.getCount() > 0) {
                    stateLoaded();
                    if (sectionHeightBasedOnChildren)
                        Utils.setListViewHeightBasedOnChildren(lvItems);
                } else {
                    stateEmpty(null);
                }

            }
        });

        lvItems = (ListView) container.findViewById(R.id.sectionBody);
        lvItems.setAdapter(adapter);

        titleWrapper = container.findViewById(R.id.sectionTitleWrapper);
        titleWrapper.setVisibility(View.GONE);
        tvTitle = titleWrapper.findViewById(R.id.sectionTitle);

        actionWrapper = container.findViewById(R.id.sectionActionWrapper);
        actionWrapper.setVisibility(View.GONE);
        tvAction = actionWrapper.findViewById(R.id.sectionAction);

        reloadWrapper = container.findViewById(R.id.sectionReload);
        emptyWrapper = container.findViewById(R.id.sectionEmpty);

        this.stateReadyToReload();
    }

    /*
    ------------------------------------
    |   Accessors & Mutators
    ------------------------------------
    */

    public void setData(List<T> data) {
        this.adapter.setData(data);
    }

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
        container.setBackgroundColor(this.backgroundColor);
    }

    public void setSectionHeightBasedOnChildren(boolean sectionHeightBasedOnChildren) {
        this.sectionHeightBasedOnChildren = sectionHeightBasedOnChildren;
        if (sectionHeightBasedOnChildren) {
            Utils.setListViewHeightBasedOnChildren(this.lvItems);
        }
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        if (dividerHeight > 0) {
            lvItems.setDividerHeight(dividerHeight);
        }
    }

    /*
    ------------------------------------
    |   Section States
    ------------------------------------
    */

    public void setState(int state) {
        switch (state) {
            case STATE_READY_TO_RELOAD:
                stateReadyToReload();
                break;
            case STATE_LOADED:
                stateLoaded();
                break;
            case STATE_EMPTY:
                stateEmpty(null);
                break;
        }
    }

    private void stateReadyToReload() {
        reloadWrapper.setVisibility(View.VISIBLE);
        emptyWrapper.setVisibility(View.GONE);
        lvItems.setVisibility(View.GONE);

        this.stateObserver.sectionReadyToReload(this.adapter);
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

    public interface SectionStateObserver<T> {
        void sectionReadyToReload(GenericListAdapter<T> adapter);

        void sectionEmpty();

        void sectionLoaded();
    }
}