package freifunk.bremen.de.mobilemeshviewer.api.manager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class ConnectionManager {

    public static final String URL_FREIFUNK = "http://downloads.bremen.freifunk.net/";
    public static final String URL_MORTZU = "http://status.ffhb.mortzu.de/";

    private Map<String, Retrofit> retrofitMap = new HashMap<>();

    @Inject
    private ConnectivityManager connectivityManager;

    public Retrofit getRetrofitFreifunkConnection() {
        return getRetrofitConnection(URL_FREIFUNK);
    }

    public Retrofit getRetrofitMortzuConnection() {
        return getRetrofitConnection(URL_MORTZU);
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