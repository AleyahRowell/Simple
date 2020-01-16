package com.example.simple;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList items;
    public static final String KEY_ITEM_TEXT ="item_text";
    public static final String KEY_ITEM_POSITION ="item_position";
    public static final int EDIT_TEXT_CODE = 20;


    Button btnAdd;
    TextView etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvitems);


        loadItems();

       ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void OnItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);


                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Items was removed",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

       ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
           @Override
           public void onItemsClick(int position) {

           }
       };

       final ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
       rvItems.setAdapter(itemsAdapter);
       rvItems.setLayoutManager(new LinearLayoutManager(this));

       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String todoItem = etItem.getText().toString();
               //Add item to the model
               items.add(todoItem);

               //Notify adapter that an item is inserted
               itemsAdapter.notifyItemInserted(items.size()-1);
               etItem.setText("");
               Toast.makeText(getApplicationContext(), "Items was added",Toast.LENGTH_SHORT).show();
               saveItems();
           }
       });



       //HELP! ItemsAdapter.OnLongClickListener onLongClickListener = (position)


    }
    private  File getDataFile() {
        //returns the file
        return new File(getFilesDir(),"data.txt");
    }
    //this function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Memory", "Error reading items ",e);
            items = new ArrayList<>();

        }
    }

    //this function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main Memory", "Error writing items", e);


        }
    }

}
