package com.example.sqlitecruds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;



import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    EditText addName;
    Button addData;
    ListView myListview;

    ArrayList<String> listitem;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        addName = findViewById(R.id.add_name);
        addData = findViewById(R.id.add_data);
        myListview = findViewById(R.id.user_list);

        listitem = new ArrayList<>();

        viewData();
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = myListview.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addName.getText().toString();
                if (!name.equals("") && databaseHelper.insertData(name)){
                    Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                    addName.setText("");
                    listitem.clear();
                    viewData();
                } else {
                    Toast.makeText(MainActivity.this, "Data not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewData() {
        Cursor cursor = databaseHelper.viewData();

        if (cursor.getCount() == 0){
            Toast.makeText(MainActivity.this,"No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                listitem.add(cursor.getString(1)); //index 1 is name, index 0 is id
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listitem);
            myListview.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem serachItem =menu.findItem(R.id.item_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(serachItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> userList = new ArrayList<>();
                /*for (String user : listitem){
                    if (user.toLowerCase().contains(newText.toLowerCase())){
                        userList.add(user);
                    }
                }*/
                if(!newText.isEmpty()){
                    userList=databaseHelper.searchUsersstring(newText);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, userList);

                    myListview.setAdapter(adapter);
                }
                else{
                    userList.clear();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, userList);

                    myListview.setAdapter(adapter);
                }



                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}











