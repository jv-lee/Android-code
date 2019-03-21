package com.lee.code.hermes.process;

import com.lee.library.hermeslib.annotion.ClassId;

/**
 * 映射子类
 */
@ClassId("com.lee.code.hermes.process.UserManager")
public interface IUserManager {
    Person getPerson();
    void setPerson(Person person);
}
