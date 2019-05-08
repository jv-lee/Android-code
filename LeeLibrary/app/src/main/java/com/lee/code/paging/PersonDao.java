package com.lee.code.paging;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert
    void insertPerson(Person person);

    @Insert
    void insertPerson(List<Person> persons);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT * FROM Person ORDER BY name COLLATE NOCASE ASC")
    DataSource.Factory<Integer, Person> getAllPersons();

    @Query("SELECT * FROM Person ORDER BY name COLLATE NOCASE ASC")
    List<Person> getAllPersons2();


}
