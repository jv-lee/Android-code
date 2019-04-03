// IDataManager.aidl
package android.aidl;

// Declare any non-default types here with import statements
import android.aidl.DataEntity;
import android.aidl.IOnDataArrivedListener;
interface IDataManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<DataEntity> findAll();
    void add(in DataEntity entity);
    void registerListener(IOnDataArrivedListener listener);
    void unRegisterListener(IOnDataArrivedListener listener);
}
