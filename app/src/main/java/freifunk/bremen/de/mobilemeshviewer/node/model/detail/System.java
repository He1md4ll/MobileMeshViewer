package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class System implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public System createFromParcel(Parcel in) {
                    return new System(in);
                }

                public System[] newArray(int size) {
                    return new System[size];
                }
            };
    @SerializedName("site_code")
    @Expose
    private String siteCode;

    public System() {
    }

    public System(Parcel in) {
        readFromParcel(in);
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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
        dest.writeString(siteCode);
    }

    private void readFromParcel(Parcel in) {
        siteCode = in.readString();
    }
}
