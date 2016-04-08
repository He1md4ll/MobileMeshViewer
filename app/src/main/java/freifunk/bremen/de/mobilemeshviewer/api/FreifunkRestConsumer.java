package freifunk.bremen.de.mobilemeshviewer.api;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetailList;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;
import retrofit.Call;
import retrofit.http.GET;

public interface FreifunkRestConsumer {

    @GET("data/nodelist.json")
    Call<NodeList> getNodeList();

    @GET("data/nodes.json")
    Call<NodeDetailList> getNodeDetailList();
}
