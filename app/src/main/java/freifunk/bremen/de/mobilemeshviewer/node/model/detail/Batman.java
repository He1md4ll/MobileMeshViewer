package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Batman implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Batman createFromParcel(Parcel in) {
                    return new Batman(in);
                }

                public Batman[] newArray(int size) {
                    return new Batman[size];
                }
            };
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("compat")
    @Expose
    private String compat;

    public Batman() {
    }

    public Batman(Parcel in) {
        readFromParcel(in);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCompat() {
        return compat;
    }

    public void setCompat(String compat) {
        this.compat = compat;
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
        dest.writeString(compat);
        dest.writeString(version);
    }

    private void readFromParcel(Parcel in) {
        compat = in.readString();
        version = in.readString();
    }
}
