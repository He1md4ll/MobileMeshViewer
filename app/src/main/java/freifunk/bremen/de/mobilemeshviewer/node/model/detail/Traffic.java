package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Traffic implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Traffic createFromParcel(Parcel in) {
                    return new Traffic(in);
                }

                public Traffic[] newArray(int size) {
                    return new Traffic[size];
                }
            };
    @SerializedName("rx")
    @Expose
    private TrafficBytes rx;
    @SerializedName("mgmt_rx")
    @Expose
    private TrafficBytes mgmtRx;
    @SerializedName("tx")
    @Expose
    private TrafficBytesDropped tx;
    @SerializedName("forward")
    @Expose
    private TrafficBytes forward;
    @SerializedName("mgmt_tx")
    @Expose
    private TrafficBytes mgmtTx;

    public Traffic() {
    }

    public Traffic(Parcel in) {
        readFromParcel(in);
    }

    public TrafficBytes getRx() {
        return rx;
    }

    public void setRx(TrafficBytes rx) {
        this.rx = rx;
    }

    public TrafficBytes getMgmtRx() {
        return mgmtRx;
    }

    public void setMgmtRx(TrafficBytes mgmtRx) {
        this.mgmtRx = mgmtRx;
    }

    public TrafficBytesDropped getTx() {
        return tx;
    }

    public void setTx(TrafficBytesDropped tx) {
        this.tx = tx;
    }

    public TrafficBytes getForward() {
        return forward;
    }

    public void setForward(TrafficBytes forward) {
        this.forward = forward;
    }

    public TrafficBytes getMgmtTx() {
        return mgmtTx;
    }

    public void setMgmtTx(TrafficBytes mgmtTx) {
        this.mgmtTx = mgmtTx;
    }

    @Override
    public String toString() {
        return true ? "Online" : "Offline";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(rx, flags);
        dest.writeParcelable(mgmtRx, flags);
        dest.writeParcelable(tx, flags);
        dest.writeParcelable(forward, flags);
        dest.writeParcelable(mgmtTx, flags);
    }

    private void readFromParcel(Parcel in) {
        rx = in.readParcelable(TrafficBytes.class.getClassLoader());
        mgmtRx = in.readParcelable(TrafficBytes.class.getClassLoader());
        tx = in.readParcelable(TrafficBytes.class.getClassLoader());
        forward = in.readParcelable(TrafficBytes.class.getClassLoader());
        mgmtTx = in.readParcelable(TrafficBytes.class.getClassLoader());
    }
}
