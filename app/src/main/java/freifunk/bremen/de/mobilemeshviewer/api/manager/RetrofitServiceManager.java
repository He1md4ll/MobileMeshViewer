package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.api.FfhbRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class RetrofitServiceManager {

    private Optional<FreifunkRestConsumer> freifunkServiceOptional = Optional.absent();
    private Optional<FfhbRestConsumer> ffhbServiceOptional = Optional.absent();


    @Inject
    private ConnectionManager connectionManager;

    public FreifunkRestConsumer getFreifunkService() throws IOException {
        if (connectionManager.isNetworkAvailable()) {
            if (!freifunkServiceOptional.isPresent()) {
                freifunkServiceOptional = Optional.of(connectionManager
                        .getRetrofitFreifunkConnection().create(FreifunkRestConsumer.class));
            }
            return freifunkServiceOptional.get();
        } else {
            throw new IOException("No network connection present!");
        }
    }

    public FfhbRestConsumer getFfhbService() throws IOException {
        if (connectionManager.isNetworkAvailable()) {
            if (!ffhbServiceOptional.isPresent()) {
                ffhbServiceOptional = Optional.of(connectionManager
                        .getRetrofitFfhbConnection().create(FfhbRestConsumer.class));
            }
            return ffhbServiceOptional.get();
        } else {
            throw new IOException("No network connection present!");
        }
    }
}