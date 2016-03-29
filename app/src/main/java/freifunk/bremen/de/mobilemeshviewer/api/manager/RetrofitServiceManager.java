package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;

@Singleton
public class RetrofitServiceManager {

    private Optional<FreifunkRestConsumer> achievementServiceOptional = Optional.absent();

    @Inject
    private ConnectionManager connectionManager;

    public FreifunkRestConsumer getFreifunkService() throws IOException {
        if (connectionManager.isNetworkAvailable()) {
            if (!achievementServiceOptional.isPresent()) {
                achievementServiceOptional = Optional.fromNullable(connectionManager
                        .getRetrofitConnection().create(FreifunkRestConsumer.class));
            }
            return achievementServiceOptional.get();
        } else {
            throw new IOException("No network connection present!");
        }
    }
}