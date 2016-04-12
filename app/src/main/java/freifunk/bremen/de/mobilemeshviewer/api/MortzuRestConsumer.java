package freifunk.bremen.de.mobilemeshviewer.api;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.CheckServer;
import retrofit.Call;
import retrofit.http.GET;

public interface MortzuRestConsumer {

    @GET("data/merged.json")
    Call<List<CheckServer>> getGatewayList();
}
