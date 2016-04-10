package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class FastD implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public FastD createFromParcel(Parcel in) {
                    return new FastD(in);
                }

                public FastD[] newArray(int size) {
                    return new FastD[size];
                }
            };
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public FastD() {
    }

    public FastD(Parcel in) {
        readFromParcel(in);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        dest.writeString(version);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        version = in.readString();
        enabled = in.readByte() != 0;
    }
}
