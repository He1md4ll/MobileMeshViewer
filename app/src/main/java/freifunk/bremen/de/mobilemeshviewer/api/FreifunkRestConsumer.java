package freifunk.bremen.de.mobilemeshviewer.api;

import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import retrofit.Call;
import retrofit.http.GET;

public interface FreifunkRestConsumer {

    @GET("data/nodelist.json")
    Call<NodeList> getNodeList();
}
