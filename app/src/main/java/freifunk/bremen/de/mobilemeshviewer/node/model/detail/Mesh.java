package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anon on 08.04.2016.
 */
public class Mesh implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Mesh createFromParcel(Parcel in) {
                    return new Mesh(in);
                }

                public Mesh[] newArray(int size) {
                    return new Mesh[size];
                }
            };

    public Mesh() {
    }

    public Mesh(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private void readFromParcel(Parcel in) {

    }
}
