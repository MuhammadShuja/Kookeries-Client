package com.kookeries.shop.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.ImageGridAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.ProductImage;
import com.kookeries.shop.utils.Glide4Engine;
import com.kookeries.shop.utils.Utils;
import com.kookeries.shop.utils.GifSizeFilter;
import com.kookeries.shop.ui.widgets.DatePickerWidget;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SellerProductNewActivity extends AppCompatActivity {

    private static final String TAG = "SellerProductNew";

    private static final int COMMISSION_PERCENTAGE_VALUE = 15;
    private static final int REQUEST_CODE_PERMISSION = 111;
    private static final int REQUEST_CODE_PICKER = 111;
    private static int VALIDATION_FLAG = 0;

    private List<Uri> imagesPicked;
    private int selectedImage;

    private EditText inputName, inputPrice, inputQuantity, inputLocation, inputDescription;
    private TextView tvEarn, tvExpiry, tvAddImage;
    private TextView tvNameError, tvPriceError, tvQuantityError, tvExpiryError, tvLocationError, tvDescriptionError, tvImageError;
    private Spinner spinner;
    private RecyclerView rvImages;
    private ImageGridAdapter imageGridAdapter;
    private List<ProductImage> productImages = new ArrayList<>();

    SweetAlertDialog dialog;

    private String[] items;

    private Category selectedCategory;
    private String name, price, earning, quantity, expiry, location, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_seller_product_new);
        API.instantiate(getApplicationContext());

        setupToolbar();
        setupForm();
        setupImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_new_product_menu, menu);
        Utils.menuIconColor(menu.findItem(R.id.save), Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            saveProduct();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                removeImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK) {
            imagesPicked = Matisse.obtainResult(data);
            Log.d("Matisse", "-------------------------- Selected Images: " + imagesPicked);
            for(Uri uri : imagesPicked){
                productImages.add(new ProductImage(uri.toString()));
            }
            imageGridAdapter.setData(productImages);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPicker();
                } else {
                    Toast.makeText(
                            SellerProductNewActivity.this,
                            "Permission denied to read your External storage.",
                            Toast.LENGTH_LONG
                    ).show();
                }

                return;
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showPicker(){
        Matisse.from(SellerProductNewActivity.this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(false, getPackageName()+".fileprovider"))
                .countable(true)
                .maxSelectable(9)
                .theme(R.style.ImagePickerTheme)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_PICKER);
    }

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setTitle("New Product");
    }

    private void setupImages(){
        tvAddImage = (TextView) findViewById(R.id.btnAddImage);
        tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if(hasPermissions(SellerProductNewActivity.this, PERMISSIONS)){
                    showPicker();
                }else{
                    ActivityCompat.requestPermissions(SellerProductNewActivity.this, PERMISSIONS, REQUEST_CODE_PERMISSION);
                }
            }
        });

        imageGridAdapter = new ImageGridAdapter(this, productImages, getMenuInflater(), new ImageGridAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, ProductImage productImage) {
                ProductImage.selected = productImage;
                view.showContextMenu();
                selectedImage = position;
            }
        });

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        rvImages.setAdapter(imageGridAdapter);
        rvImages.setNestedScrollingEnabled(false);

        Drawable divider = ContextCompat.getDrawable(this, R.drawable.image_grid_divider);
        DividerItemDecoration horizontalDivider = new DividerItemDecoration(rvImages.getContext(), DividerItemDecoration.HORIZONTAL);
        DividerItemDecoration verticalDivider = new DividerItemDecoration(rvImages.getContext(), DividerItemDecoration.VERTICAL);
        horizontalDivider.setDrawable(divider);
        verticalDivider.setDrawable(divider);
        rvImages.addItemDecoration(horizontalDivider);
        rvImages.addItemDecoration(verticalDivider);

        registerForContextMenu(rvImages);
    }

    private void setupForm(){
        setupCategoriesSpinner();

        inputName = (EditText) findViewById(R.id.name);
        inputPrice = (EditText) findViewById(R.id.price);
        inputQuantity = (EditText) findViewById(R.id.quantity);
        inputLocation = (EditText) findViewById(R.id.location);
        inputDescription = (EditText) findViewById(R.id.description);

        tvEarn = (TextView) findViewById(R.id.earning);
        tvExpiry = (TextView) findViewById(R.id.expiry);

        tvNameError = (TextView) findViewById(R.id.name_error);
        tvPriceError = (TextView) findViewById(R.id.price_error);
        tvQuantityError = (TextView) findViewById(R.id.quantity_error);
        tvExpiryError = (TextView) findViewById(R.id.expiry_error);
        tvLocationError = (TextView) findViewById(R.id.location_error);
        tvDescriptionError = (TextView) findViewById(R.id.description_error);
        tvImageError = (TextView) findViewById(R.id.image_error);

        inputPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double val = 0;
                if(charSequence.length() > 0){
                    try{
                        val = Double.parseDouble(charSequence.toString());
                        val -= val * COMMISSION_PERCENTAGE_VALUE / 100;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                tvEarn.setText(String.valueOf(val));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        DatePickerWidget datePickerWidget = new DatePickerWidget(tvExpiry, this);
    }

    private void validateForm(){
        name = inputName.getText().toString();
        price = inputPrice.getText().toString();
        earning = tvEarn.getText().toString();
        quantity = inputQuantity.getText().toString();
        expiry = tvExpiry.getText().toString();
        location = inputLocation.getText().toString();
        description = inputDescription.getText().toString();

//        PRODUCT NAME
        if(name.length() == 0){
            tvNameError.setVisibility(View.VISIBLE);
            tvNameError.setText("Product name is required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvNameError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
//        PRODUCT PRICE
        if(price.length() == 0){
            tvPriceError.setVisibility(View.VISIBLE);
            tvPriceError.setText("Product price is required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvPriceError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
//        PRODUCT QUANTITY
        if(quantity.length() == 0){
            tvQuantityError.setVisibility(View.VISIBLE);
            tvQuantityError.setText("Product quantity is required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvQuantityError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
//        PRODUCT EXPIRY
        if(expiry.length() == 0){
            tvExpiryError.setVisibility(View.VISIBLE);
            tvExpiryError.setText("Product expiry is required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvExpiryError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
//        PRODUCT LOCATION
        if(location.length() == 0){
            tvLocationError.setVisibility(View.VISIBLE);
            tvLocationError.setText("Product location is required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvLocationError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
//        PRODUCT IMAGES
        if(productImages.size() < 1){
            tvImageError.setVisibility(View.VISIBLE);
            tvImageError.setText("Product image(s) are required");
            VALIDATION_FLAG = 1;
        }
        else{
            tvImageError.setVisibility(View.GONE);
            VALIDATION_FLAG = 0;
        }
    }

    private void setupCategoriesSpinner(){
        API.getCategories(new ApiResponse.CatalogListener<Category>() {
            @Override
            public void onSuccess(final List<Category> data) {
                items = new String[data.size()];
                for(int i = 0; i < data.size() ; i++){
                    items[i] = data.get(i).getName();
                }

                spinner = (Spinner) findViewById(R.id.category);
                ArrayAdapter adapter = new ArrayAdapter<String>(SellerProductNewActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        ((TextView) adapterView.getChildAt(0)).setTextSize(14);
                        (adapterView.getChildAt(0)).setPadding(0, 0, 0, 0);

                        selectedCategory = data.get(i);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(JSONObject response) {

            }

            @Override
            public void onException(Exception e) {

            }
        });
    }

    private void removeImage(){
        productImages.remove(selectedImage);
        imageGridAdapter.setData(productImages);
    }

    private void saveProduct(){
        validateForm();

        if(VALIDATION_FLAG == 0){
            showProgressDialog();
            Product productToSave = Product.Saleable(selectedCategory, name, price, earning, quantity, expiry, location, description, productImages);

            API.newProduct(productToSave, new ApiResponse.ProductListener() {
                @Override
                public void onSuccess(Product product) {
                    showSuccessDialog(product);
                }

                @Override
                public void onFailure(JSONObject response) {
                    showErrorDialog(response.toString());
                }

                @Override
                public void onException(Exception e) {
                    showErrorDialog(e.getMessage());
                }
            });
        }
    }

    private void showProgressDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primaryColor));
        dialog.setTitleText("Saving product");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showSuccessDialog(final Product product) {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("Saved");
        dialog.setConfirmText("View");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Product.selected = product;
                startActivity(new Intent(SellerProductNewActivity.this, ProductActivity.class));
                finish();
            }
        });
    }

    private void showErrorDialog(String errorMessage) {
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("Error");
        dialog.setContentText(errorMessage);
        dialog.setCancelable(true);
    }
}