package com.example.starbuck;

import android.content.Context;

import androidx.lifecycle.ViewModel;

public class FruitViewModel extends ViewModel {

    Repository repository;

    FruitDAO fruitDAO;
    private FruitDatabase fruitDB;

    //context???
    public FruitViewModel(Context context){
        //创建数据库,其实可以推迟数据库的创建时间
/*        FruitDatabase fruitDatabase=FruitDatabase.getFruitDataBase(context);
        FruitDAO fruitDAO=fruitDatabase.getFruitDao();*/

        // 使用repository
        repository=new Repository();
    }

    //delegate , 用dao传进去
    public void insertFruit(Fruit fruit){
        fruitDAO.insertFruit(fruit);
    }
}
