package freifunk.bremen.de.mobilemeshviewer.api;


import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FreifunkRestConsumer {

    @GET("data/nodelist.json")
    Call<NodeList> getNodeList();

    @GET("data/nodes.json")
    Call<ResponseBody> getNodeDetailList();
}
