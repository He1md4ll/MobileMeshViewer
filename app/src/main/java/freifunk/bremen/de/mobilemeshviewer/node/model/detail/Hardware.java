package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Hardware implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Hardware createFromParcel(Parcel in) {
                    return new Hardware(in);
                }

                public Hardware[] newArray(int size) {
                    return new Hardware[size];
                }
            };
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("nproc")
    @Expose
    private String nproc;

    public Hardware() {
    }

    public Hardware(Parcel in) {
        readFromParcel(in);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNproc() {
        return nproc;
    }

    public void setNproc(String nproc) {
        this.nproc = nproc;
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
        dest.writeString(model);
        dest.writeString(nproc);
    }

    private void readFromParcel(Parcel in) {
        model = in.readString();
        nproc = in.readString();
    }
}
