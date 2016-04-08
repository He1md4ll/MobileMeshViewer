package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anon on 08.04.2016.
 */
public class Nodeinfo implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Nodeinfo createFromParcel(Parcel in) {
                    return new Nodeinfo(in);
                }

                public Nodeinfo[] newArray(int size) {
                    return new Nodeinfo[size];
                }
            };

    public Nodeinfo() {
    }

    public Nodeinfo(Parcel in) {
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
