package freifunk.bremen.de.mobilemeshviewer.api;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.CheckServer;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FfhbRestConsumer {

    @GET("data/merged.json")
    Call<List<CheckServer>> getGatewayList();
}
