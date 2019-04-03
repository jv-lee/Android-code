// IOnDataArrivedListener.aidl
package android.aidl;

// Declare any non-default types here with import statements
import android.aidl.DataEntity;
interface IOnDataArrivedListener {
   void onDataArrivedListener(in DataEntity entity);
}
