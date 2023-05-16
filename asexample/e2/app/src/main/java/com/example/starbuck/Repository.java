package com.example.starbuck;

import java.util.List;

public class Repository {
    private FruitDAO fruitDAO;
    private FruitDatabase fruitDB;

    public Repository(){
        fruitDB=FruitDatabase.getFruitDataBase();
        fruitDAO=fruitDB.getFruitDao();
    }
    public List<Fruit> searchByName(String  name){
        return fruitDAO.getFruitByName(name);
    }
}
