package com.lee.code.paging;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import com.lee.library.utils.ThreadUtil;

import java.util.Arrays;
import java.util.List;

@Database(entities = Person.class,version = 1)
abstract class PersonDatabase extends RoomDatabase {
    abstract PersonDao personDao();

    private static PersonDatabase instance;
    PersonDatabase(){}
    static PersonDatabase getInstance(Context context){
        Log.i(">>>", "getInstance()");
        if (instance == null) {
            Log.i(">>>", "getInstance() == null");
            instance = Room.databaseBuilder(context.getApplicationContext(), PersonDatabase.class, "personDatabase")
                    .allowMainThreadQueries()
                    .addCallback( new RoomDatabase.Callback(){
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            Log.i(">>>", "database onCreate()");
                            ThreadUtil.getInstance().addTask(() -> {
                                for (int i = 0; i < getData().size(); i++) {
                                    Person person = new Person();
                                    person.name = getData().get(i);
                                    getInstance(context).personDao().insertPerson(person);
                                }
                            });
                        }
                    }).build();
        }
        return instance;
    }

    public static List<String> getData(){
        String[] arr = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
                "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag",
                "Airedale", "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert",  // 15
                "American Cheese", "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro",
                "Appenzell", "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String",
                "Aromes au Gene de Marc", "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", // 30
                "Avaxtskyr", "Baby Swiss", "Babybel", "Baguette Laonnaise", "Bakers",
                "Baladi", "Balaton", "Bandal", "Banon", "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
                "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
                "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
                "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
                "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
                "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)"};
        List<String> data = Arrays.asList(arr);
        return data;
    }

}
