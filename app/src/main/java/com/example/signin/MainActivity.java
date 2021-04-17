
package com.example.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final String[] stocks = {"IBM","AAPL","GOOGL","AMZN","TSLA"};
    private ArrayList<Stock> theStocks;
    RecyclerView example;
    StockRecyclerAdapter exampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theStocks = new ArrayList<Stock>();
        example = findViewById(R.id.recycler);

        for(String s : stocks) {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={{REPLACE_KEY}}&apikey=PK0N9ES6VOC7CHZL".replace("{{REPLACE_KEY}}",s);
            RequestQueue queue = Volley.newRequestQueue(this);


            GsonRequest<GlobalQuote> req = new GsonRequest<GlobalQuote>(url, GlobalQuote.class, null, new Response.Listener<GlobalQuote>() {
                @Override
                public void onResponse(GlobalQuote response) {

                    System.out.println("Successful Response");
                    System.out.println(response.getGlobalQuote().getSymbol());
                    theStocks.add(response.getGlobalQuote());

                    if(theStocks.size() == stocks.length){
                        doView();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("That didn't work!");
                }
            });

            queue.add(req);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.lookup:
                        return true;
                    case R.id.favorites:

                        return true;


                }
                return false;
            }
        });
    }
    private void doView(){
        exampleAdapter = new StockRecyclerAdapter(theStocks);
        example.setAdapter(exampleAdapter);
        example.setLayoutManager(new LinearLayoutManager(this));
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
}