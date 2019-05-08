package com.lee.code.paging;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Person {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
}
