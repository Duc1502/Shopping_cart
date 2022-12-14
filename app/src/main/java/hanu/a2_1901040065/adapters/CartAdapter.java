package hanu.a2_1901040065.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

import hanu.a2_1901040065.CurrencyFormatter;
import hanu.a2_1901040065.R;
import hanu.a2_1901040065.RecyclerViewItemClick;
import hanu.a2_1901040065.db.PManager;
import hanu.a2_1901040065.models.Product;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>{

    private List<Product> products;
    private RecyclerViewItemClick myClickListener;
    private PManager pManager;
    private Context context;
    private Product product;


    public CartAdapter(List<Product> products, PManager pManager) {
        this.products = products;
        this.pManager = pManager;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item, parent, false );
        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder{
        private ImageView pImg;
        private TextView pName;
        private TextView pPrice;
        private TextView pQuantity;
        private ImageView addProduct;
        private ImageView minusProduct;
        private TextView sumPrice;


        public CartHolder(@NonNull View itemView) {
            super(itemView);

            pImg = itemView.findViewById(R.id.product_img);
            pName = itemView.findViewById(R.id.product_name);
            pPrice = itemView.findViewById(R.id.product_price);
            pQuantity = itemView.findViewById(R.id.product_quantity);
            addProduct = itemView.findViewById(R.id.add);
            minusProduct = itemView.findViewById(R.id.minus);
            sumPrice = itemView.findViewById(R.id.sum_price);
        }

        public void bind(Product product){
            new DownloadImageTask(pImg).execute(product.getImgUrl());
            pName.setText(product.getName());
            pPrice.setText(CurrencyFormatter.format((long) product.getPrice()));
            pQuantity.setText(String.valueOf(product.getQuantity()));
            sumPrice.setText(CurrencyFormatter.format((long) product.getPrice() * product.getQuantity()));
            //handle click event
            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = product.getQuantity();
                    product.setQuantity(currentQuantity + 1);
                    pManager.update(product);
                    myClickListener.onItemClick(getAdapterPosition(), v);
                    notifyDataSetChanged();
                }
            });

            minusProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = product.getQuantity();

                    if (quantity - 1 == 0){
                        pManager.delete((long) product.getId());
                        products.remove(product);
                    }else {
                        product.setQuantity(quantity - 1);
                        pManager.update(product);
                    }

                    myClickListener.onItemClick(getAdapterPosition(), v);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void onItemClick(RecyclerViewItemClick mclick){
        this.myClickListener = mclick;
    }

    public void updateList(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
