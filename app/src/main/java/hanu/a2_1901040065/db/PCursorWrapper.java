package hanu.a2_1901040065.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040065.models.Product;


public class PCursorWrapper extends CursorWrapper {
    public PCursorWrapper(Cursor cursor) {

        super(cursor);
    }
    public Product getProduct(){
        Product product = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String name = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME));
        String imgUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMG_URL));
        int price = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY));

        if(id != 0 && valid(name) && valid(imgUrl) && price > 0 && quantity > 0)
            product = new Product(id, imgUrl, name, price, quantity);
        return product;
    }

    public Product getAProduct(){
        moveToFirst();

        if(getCount()==0){
            return null;
        }

        Product product = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String name = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME));
        String imgUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMG_URL));
        int price = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY));

        if(id != 0 && valid(name) && valid(imgUrl) && price > 0 && quantity > 0)
            product = new Product(id, imgUrl, name, price, quantity);
        return product;
    }


    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        moveToFirst();
        while(!isAfterLast()){
            Product product = getProduct();
            products.add(product);
            moveToNext();
        }
        return products;
    }

    private boolean valid(String string){
        return string != null && string.length() > 0;
    }
    
}
