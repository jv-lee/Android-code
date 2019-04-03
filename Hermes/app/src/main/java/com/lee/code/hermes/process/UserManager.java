package com.lee.code.hermes.process;

/**
 * 单列必须实现接口
 */
public class UserManager implements IUserManager {
    private Person person;
    private static UserManager sInstance = null;
    private UserManager(){

    }

    //架构约束
    public static synchronized UserManager getInstance(){
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    @Override
    public Person getPerson(){
        return person;
    }

    @Override
    public void setPerson(Person person) {
        this.person = person;
    }

}
