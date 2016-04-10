package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Statuspage implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Statuspage createFromParcel(Parcel in) {
                    return new Statuspage(in);
                }

                public Statuspage[] newArray(int size) {
                    return new Statuspage[size];
                }
            };
    @SerializedName("api")
    @Expose
    private int api;

    public Statuspage() {
    }

    public Statuspage(Parcel in) {
        readFromParcel(in);
    }

    public int getApi() {
        return api;
    }

    public void setApi(int api) {
        this.api = api;
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
        dest.writeInt(api);
    }

    private void readFromParcel(Parcel in) {
        api = in.readInt();
    }
}
