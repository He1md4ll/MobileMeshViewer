package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("long")
    @Expose
    public Double _long;
    @SerializedName("lat")
    @Expose
    public Double lat;
}