package hanu.a2_1901040065.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

import hanu.a2_1901040065.CurrencyFormatter;
import hanu.a2_1901040065.R;
import hanu.a2_1901040065.db.PManager;
import hanu.a2_1901040065.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products;
    private PManager pManager;
    private Context context;

    public ProductAdapter(List<Product> products, PManager pManager) {
        this.products = products;
        this.pManager = pManager;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.product_item, parent, false );

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductHolder extends RecyclerView.ViewHolder{
        private ImageView pImage;
        private TextView pName;
        private TextView pPrice;
        private ImageButton cart;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            cart = itemView.findViewById(R.id.add_to_card);
        }

        public void bind(Product product){
            new DownloadImageTask(pImage).execute(product.getImgUrl());
            pName.setText(product.getName());
            pPrice.setText(CurrencyFormatter.format((long) product.getPrice()));

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pManager.add(product);
                    Toast toast= Toast.makeText(itemView.getContext(),"ADDED",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public void updateList(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }
}
