package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class Gateway implements Parcelable {
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Gateway createFromParcel(Parcel in) {
                    return new Gateway(in);
                }

                public Gateway[] newArray(int size) {
                    return new Gateway[size];
                }
            };
    private String name;
    private IpStatus ntp;
    private IpStatus addresses;
    private IpStatus dns;
    private IpStatus uplink;

    public Gateway(Parcel in) {
        readFromParcel(in);
    }

    public Gateway(String name, IpStatus ntp, IpStatus addresses, IpStatus dns, IpStatus uplink) {
        this.name = name;
        this.ntp = ntp;
        this.addresses = addresses;
        this.dns = dns;
        this.uplink = uplink;
    }

    public static Gateway fromGatewayBO(GatewayBO gatewayBO, int breakEven) {
        final IpStatus ntpStatus = IpStatus.determineStatus(gatewayBO.getNtpSum4(), gatewayBO.getNtpSum6(), breakEven);
        final IpStatus addressesStatus = IpStatus.determineStatus(gatewayBO.getAddressesSum4(), gatewayBO.getAddressesSum6(), breakEven);
        final IpStatus dnsStatus = IpStatus.determineStatus(gatewayBO.getDnsSum4(), gatewayBO.getDnsSum6(), breakEven);
        final IpStatus uplinkStatus = IpStatus.determineStatus(gatewayBO.getUplinkSum4(), gatewayBO.getUplinkSum6(), breakEven);
        return new Gateway(gatewayBO.getName(), ntpStatus, addressesStatus, dnsStatus, uplinkStatus);
    }

    public String getName() {
        return name;
    }

    public IpStatus getNtp() {
        return ntp;
    }

    public IpStatus getAddresses() {
        return addresses;
    }

    public IpStatus getDns() {
        return dns;
    }

    public IpStatus getUplink() {
        return uplink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(ntp, flags);
        dest.writeParcelable(addresses, flags);
        dest.writeParcelable(dns, flags);
        dest.writeParcelable(uplink, flags);
    }

    private void readFromParcel(Parcel in) {
        name = in.readString();
        ntp = in.readParcelable(IpStatus.class.getClassLoader());
        addresses = in.readParcelable(IpStatus.class.getClassLoader());
        dns = in.readParcelable(IpStatus.class.getClassLoader());
        uplink = in.readParcelable(IpStatus.class.getClassLoader());
    }

    @Override
    public String toString() {
        return name + " [" + uplink + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gateway)) return false;
        Gateway gateway = (Gateway) o;
        return Objects.equal(name, gateway.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}