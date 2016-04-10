package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Location implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Location createFromParcel(Parcel in) {
                    return new Location(in);
                }

                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("altitude")
    @Expose
    private String altitude;

    public Location() {
    }

    public Location(Parcel in) {
        readFromParcel(in);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
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
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(altitude);
    }

    private void readFromParcel(Parcel in) {
        longitude = in.readString();
        latitude = in.readString();
        altitude = in.readString();
    }
}
