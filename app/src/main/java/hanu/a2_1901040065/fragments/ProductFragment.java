package hanu.a2_1901040065.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040065.R;
import hanu.a2_1901040065.adapters.ProductAdapter;
import hanu.a2_1901040065.db.PManager;
import hanu.a2_1901040065.models.Product;


public class ProductFragment extends Fragment {
    private RecyclerView productRv;
    private List<Product> shoppingProducts;
    private TextView searchProducts;
    private ProductAdapter productAdapter;
    private PManager pManager;

    public ProductFragment(List<Product> shoppingProducts, PManager pManager){
        this.shoppingProducts = shoppingProducts;
        this.pManager = pManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        searchProducts = view.findViewById(R.id.search_text);

        productRv = view.findViewById(R.id.product_rv);

        productAdapter = new ProductAdapter(shoppingProducts, pManager);
        productRv.setAdapter(productAdapter);


        productRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        searchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return view;
    }

    private void filter(String text){
        List<Product> temp = new ArrayList();
        for(Product product: shoppingProducts){
            if(product.getName().toLowerCase().contains(text)){
                temp.add(product);
            }
        }
        //update recyclerview
        productAdapter.updateList(temp);
    }

}
