package hanu.a2_1901040065.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040065.CurrencyFormatter;
import hanu.a2_1901040065.R;
import hanu.a2_1901040065.RecyclerViewItemClick;
import hanu.a2_1901040065.adapters.CartAdapter;
import hanu.a2_1901040065.db.PManager;
import hanu.a2_1901040065.models.Product;


public class CartFragment extends Fragment {
    private RecyclerView cartRv;
    private CartAdapter cartAdapter;
    private TextView totalPrice;
    private List<Product> carts;
    private PManager pManager;

    public CartFragment(List<Product> carts, PManager pManager){
        this.carts = carts;
        this.pManager = pManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        cartRv = view.findViewById(R.id.cart_rv);
        TextView total = view.findViewById(R.id.total);
        totalPrice = view.findViewById(R.id.total_price);

        totalPrice.setText(CurrencyFormatter.format((long) calculateTotalPrice()));


        cartAdapter = new CartAdapter(carts, pManager);
        cartRv.setAdapter(cartAdapter);

        cartRv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    private int calculateTotalPrice(){
        int totalPrice = 0;

        for (Product product: carts){
            totalPrice += product.getPrice() * product.getQuantity();
        }

        return totalPrice;
    }

    @Override
    public void onResume(){
        super.onResume();
        cartAdapter.onItemClick(new RecyclerViewItemClick() {
            @Override
            public void onItemClick(int position, View v) {
                totalPrice.setText(CurrencyFormatter.format((long) calculateTotalPrice()));
            }
        });
    }

    public void refresh(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
}
