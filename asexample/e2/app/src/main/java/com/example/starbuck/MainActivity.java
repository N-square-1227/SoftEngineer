package com.example.starbuck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resultView=findViewById(R.id.textview);

       // FruitViewModel fruitViewModel=new FruitViewModel(new Fruit());

        FruitDatabase fruitDatabase=FruitDatabase.getFruitDataBase(this);
        FruitDAO fruitDAO=fruitDatabase.getFruitDao();

        fruitDAO.insertFruit(new Fruit("orange",2));
        fruitDAO.insertFruit(new Fruit("apple",2));

        fruitDAO.getFruitByName("orange");
        String temp="";
        for(Fruit fruit:fruitDAO.getFruitByName("orange")){
            temp+=fruit.getName();
        }
        //resultView.setText(temp);
    }
}