package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum IpStatus implements Parcelable {
    IPv4,
    IPv6,
    BOTH,
    NONE;

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public IpStatus createFromParcel(Parcel in) {
                    return values()[in.readInt()];
                }

                public IpStatus[] newArray(int size) {
                    return new IpStatus[size];
                }
            };

    public static IpStatus determineStatus(int ipv4, int ipv6, int breakpoint) {
        IpStatus status;
        if (ipv4 >= breakpoint && ipv6 >= breakpoint) {
            status = BOTH;
        } else if (ipv4 >= breakpoint) {
            status = IPv4;
        } else if (ipv6 >= breakpoint) {
            status = IPv6;
        } else {
            status = NONE;
        }
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }
}
