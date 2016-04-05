package freifunk.bremen.de.mobilemeshviewer.model.simple;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Position createFromParcel(Parcel in) {
                    return new Position(in);
                }

                public Position[] newArray(int size) {
                    return new Position[size];
                }
            };
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public Position() {
    }

    public Position(Parcel in) {
        readFromParcel(in);
    }

    public Double get_long() {
        return _long;
    }

    public void set_long(Double _long) {
        this._long = _long;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(_long);
        dest.writeDouble(lat);
    }

    private void readFromParcel(Parcel in) {
        _long = in.readDouble();
        lat = in.readDouble();
    }
}