package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Owner implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Owner createFromParcel(Parcel in) {
                    return new Owner(in);
                }

                public Owner[] newArray(int size) {
                    return new Owner[size];
                }
            };
    @SerializedName("contact")
    @Expose
    private String contact;

    public Owner() {
    }

    public Owner(Parcel in) {
        readFromParcel(in);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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
        dest.writeString(contact);
    }

    private void readFromParcel(Parcel in) {
        contact = in.readString();
    }
}
