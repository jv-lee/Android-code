package android.aidl;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * @author jv.lee
 * @date 2019/4/3
 */
public class DataEntity implements Parcelable {
    private String id;
    private String name;

    public DataEntity(){

    }

    public DataEntity(Parcel source) {
        id = source.readString();
        name = source.readString();
    }

    public DataEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
        @Override
        public DataEntity createFromParcel(Parcel source) {
            return new DataEntity(source);
        }

        @Override
        public DataEntity[] newArray(int size) {
            return new DataEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}

