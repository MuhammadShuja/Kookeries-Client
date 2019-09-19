package com.kookeries.shop.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.CartAdapter;
import com.kookeries.shop.models.Cart;
import com.kookeries.shop.models.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class CartFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView tvTitle, tvSubTotal, tvTotal;

    private List<Product> products = new ArrayList<>();

    public CartFragment() {
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        tvTitle = (TextView) view.findViewById(R.id.title);
        tvSubTotal = (TextView) view.findViewById(R.id.subTotal);
        tvTotal = (TextView) view.findViewById(R.id.total);

        populateData();

        ListView lvItems = (ListView) view.findViewById(R.id.items);
        lvItems.setAdapter(new CartAdapter(getContext(), products, R.layout.card_cart_item));

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
        products.clear();
        products.addAll(Cart.getItems());

        tvSubTotal.setText(getString(R.string.currency_symbol)+" "+Cart.getSubTotal());
        tvTotal.setText(getString(R.string.currency_symbol)+" "+Cart.getTotal());

        tvTitle.setText("Total items ("+products.size()+")");
    }
}
