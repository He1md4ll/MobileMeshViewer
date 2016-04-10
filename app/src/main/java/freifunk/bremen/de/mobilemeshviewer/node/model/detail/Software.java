package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Software implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Software createFromParcel(Parcel in) {
                    return new Software(in);
                }

                public Software[] newArray(int size) {
                    return new Software[size];
                }
            };

    public Software() {
    }

    public Software(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("batman-adv")
    @Expose
    private Batman batman;
    @SerializedName("autoupdater")
    @Expose
    private Autoupdater autoupdater;
    @SerializedName("status-page")
    @Expose
    private Statuspage statuspage;
    @SerializedName("fastd")
    @Expose
    private FastD fastD;
    @SerializedName("firmware")
    @Expose
    private Firmware firmware;

    public Batman getBatman() {
        return batman;
    }

    public void setBatman(Batman batman) {
        this.batman = batman;
    }

    public Autoupdater getAutoupdater() {
        return autoupdater;
    }

    public void setAutoupdater(Autoupdater autoupdater) {
        this.autoupdater = autoupdater;
    }

    public Statuspage getStatuspage() {
        return statuspage;
    }

    public void setStatuspage(Statuspage statuspage) {
        this.statuspage = statuspage;
    }

    public FastD getFastD() {
        return fastD;
    }

    public void setFastD(FastD fastD) {
        this.fastD = fastD;
    }

    public Firmware getFirmware() {
        return firmware;
    }

    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
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
        dest.writeParcelable(batman, flags);
        dest.writeParcelable(autoupdater, flags);
        dest.writeParcelable(statuspage, flags);
        dest.writeParcelable(fastD, flags);
        dest.writeParcelable(firmware, flags);
    }

    private void readFromParcel(Parcel in) {
        batman = in.readParcelable(Batman.class.getClassLoader());
        autoupdater = in.readParcelable(Autoupdater.class.getClassLoader());
        statuspage = in.readParcelable(Statuspage.class.getClassLoader());
        fastD = in.readParcelable(FastD.class.getClassLoader());
        firmware = in.readParcelable(Firmware.class.getClassLoader());
    }
}
