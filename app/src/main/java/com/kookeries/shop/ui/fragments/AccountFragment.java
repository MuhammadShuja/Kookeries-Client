package com.kookeries.shop.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.User;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    private TextView tvName, tvBalance, tvEmail, tvPhone, tvAddress;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        tvName = (TextView) view.findViewById(R.id.name);
        tvBalance = (TextView) view.findViewById(R.id.balance);
        tvEmail = (TextView) view.findViewById(R.id.email);
        tvPhone = (TextView) view.findViewById(R.id.phone);
        tvAddress = (TextView) view.findViewById(R.id.address);

        populateData();

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

    private void populateData(){
        User.getUser(new User.DataReadyListener() {
            @Override
            public void onReady(User user) {
                Log.d(TAG, API.PRELOG_CHECK + "inside ready "+user);
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
                tvBalance.setText(String.valueOf(user.getWallet().getBalance()));
            }
        });
    }
}
