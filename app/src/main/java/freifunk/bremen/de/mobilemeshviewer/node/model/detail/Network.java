package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Network implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public Network createFromParcel(Parcel in) {
                    return new Network(in);
                }

                public Network[] newArray(int size) {
                    return new Network[size];
                }
            };
    @SerializedName("addresses")
    @Expose
    private List<String> addresses;
    @SerializedName("mesh_interfaces")
    @Expose
    private List<String> meshInterfaces;
    @SerializedName("mesh")
    @Expose
    private Mesh mesh;
    @SerializedName("mac")
    @Expose
    private String mac;

    public Network() {
    }

    public Network(Parcel in) {
        readFromParcel(in);
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<String> getMeshInterfaces() {
        return meshInterfaces;
    }

    public void setMeshInterfaces(List<String> meshInterfaces) {
        this.meshInterfaces = meshInterfaces;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(addresses);
        dest.writeStringList(meshInterfaces);
        dest.writeParcelable(mesh, flags);
        dest.writeString(mac);
    }

    private void readFromParcel(Parcel in) {
        in.readStringList(addresses);
        in.readStringList(meshInterfaces);
        mesh = in.readParcelable(Mesh.class.getClassLoader());
        mac = in.readString();
    }
}
