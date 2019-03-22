package com.lee.code.childapp;

import com.lee.code.hermes.process.Person;
import com.lee.library.hermeslib.annotion.ClassId;

@ClassId("com.lee.code.hermes.process.UserManager")
public interface IUserManager {
    Person getPerson();

    void setPerson(Person person);
}
