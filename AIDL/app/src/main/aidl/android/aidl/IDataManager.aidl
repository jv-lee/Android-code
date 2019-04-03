// IDataManager.aidl
package android.aidl;

import android.aidl.DataEntity;
import android.aidl.IOnDataArrivedListener;
interface IDataManager {
    List<DataEntity> findAll();
    void add(in DataEntity entity);
    void registerListener(IOnDataArrivedListener listener);
    void unRegisterListener(IOnDataArrivedListener listener);
}
