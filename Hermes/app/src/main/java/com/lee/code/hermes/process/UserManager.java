package com.lee.code.hermes.process;

/**
 * 单列必须实现接口
 */
public class UserManager implements IUserManager {
    Person person;
    private static UserManager sInstance = null;
    private UserManager(){

    }

    //架构约束
    public static synchronized UserManager getsInstance(){
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    public Person getPerson(){
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
