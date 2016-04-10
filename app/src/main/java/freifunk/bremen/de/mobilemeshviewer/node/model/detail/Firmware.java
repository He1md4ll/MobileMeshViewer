package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Firmware implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Firmware createFromParcel(Parcel in) {
                    return new Firmware(in);
                }

                public Firmware[] newArray(int size) {
                    return new Firmware[size];
                }
            };
    @SerializedName("release")
    @Expose
    private String release;
    @SerializedName("base")
    @Expose
    private String base;

    public Firmware() {
    }

    public Firmware(Parcel in) {
        readFromParcel(in);
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
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
        dest.writeString(release);
        dest.writeString(base);
    }

    private void readFromParcel(Parcel in) {
        release = in.readString();
        base = in.readString();
    }
}
