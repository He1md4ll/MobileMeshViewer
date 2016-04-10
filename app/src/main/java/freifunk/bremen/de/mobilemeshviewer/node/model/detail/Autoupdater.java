package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Autoupdater implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Autoupdater createFromParcel(Parcel in) {
                    return new Autoupdater(in);
                }

                public Autoupdater[] newArray(int size) {
                    return new Autoupdater[size];
                }
            };
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("branch")
    @Expose
    private String branch;

    public Autoupdater() {
    }

    public Autoupdater(Parcel in) {
        readFromParcel(in);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
        dest.writeString(branch);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        branch = in.readString();
        enabled = in.readByte() != 0;
    }
}
