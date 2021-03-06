package freifunk.bremen.de.mobilemeshviewer.api.manager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionManager {

    public static final String URL_FREIFUNK = "https://downloads.bremen.freifunk.net/";
    public static final String URL_FFHB = "https://status.ffhb.de/";

    private Map<String, Retrofit> retrofitMap = new HashMap<>();
    private ConnectivityManager connectivityManager;

    @Inject
    public ConnectionManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public Retrofit getRetrofitFreifunkConnection() {
        return getRetrofitConnection(URL_FREIFUNK);
    }

    public Retrofit getRetrofitFfhbConnection() {
        return getRetrofitConnection(URL_FFHB);
    }

    public Retrofit getRetrofitConnection(String url) {
        Retrofit retrofit = retrofitMap.get(url);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            retrofitMap.put(url, retrofit);
        }
        return retrofit;
    }

    public boolean isNetworkAvailable() {
        Optional<NetworkInfo> activeNetworkInfo = Optional.fromNullable(connectivityManager.getActiveNetworkInfo());
        return activeNetworkInfo.isPresent() && activeNetworkInfo.get().isConnected();
    }
}