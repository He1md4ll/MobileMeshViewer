package freifunk.bremen.de.mobilemeshviewer.api.manager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Singleton
public class ConnectionManager {

    public static final String URL_ALL_ACHIEVEMENTS = "http://chaos-krauts.de/Achievement/";
    private Optional<Retrofit> retrofitOptional = Optional.absent();

    @Inject
    private ConnectivityManager connectivityManager;

    public Retrofit getRetrofitConnection() {
        if (!retrofitOptional.isPresent()) {
            retrofitOptional = Optional.of(new Retrofit.Builder()
                    .baseUrl(URL_ALL_ACHIEVEMENTS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build());
        }
        return retrofitOptional.get();
    }

    public boolean isNetworkAvailable() {
        Optional<NetworkInfo> activeNetworkInfo = Optional.fromNullable(connectivityManager.getActiveNetworkInfo());
        return activeNetworkInfo.isPresent() && activeNetworkInfo.get().isConnected();
    }
}