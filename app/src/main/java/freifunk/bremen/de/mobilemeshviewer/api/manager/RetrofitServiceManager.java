package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class RetrofitServiceManager {

    private Optional<FreifunkRestConsumer> freifunkServiceOptional = Optional.absent();
    private Optional<MortzuRestConsumer> mortzuServiceOptional = Optional.absent();


    @Inject
    private ConnectionManager connectionManager;

    public FreifunkRestConsumer getFreifunkService() throws IOException {
        if (connectionManager.isNetworkAvailable()) {
            if (!freifunkServiceOptional.isPresent()) {
                freifunkServiceOptional = Optional.fromNullable(connectionManager
                        .getRetrofitFreifunkConnection().create(FreifunkRestConsumer.class));
            }
            return freifunkServiceOptional.get();
        } else {
            throw new IOException("No network connection present!");
        }
    }

    public MortzuRestConsumer getMortzuService() throws IOException {
        if (connectionManager.isNetworkAvailable()) {
            if (!mortzuServiceOptional.isPresent()) {
                mortzuServiceOptional = Optional.fromNullable(connectionManager
                        .getRetrofitMortzuConnection().create(MortzuRestConsumer.class));
            }
            return mortzuServiceOptional.get();
        } else {
            throw new IOException("No network connection present!");
        }
    }
}