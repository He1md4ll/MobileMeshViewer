package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;

import java.io.IOException;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.api.FfhbRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;

public class RetrofitServiceManager {

    private Optional<FreifunkRestConsumer> freifunkServiceOptional = Optional.absent();
    private Optional<FfhbRestConsumer> ffhbServiceOptional = Optional.absent();
    private ConnectionManager connectionManager;

    @Inject
    public RetrofitServiceManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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