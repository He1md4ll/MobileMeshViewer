package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayListLoader extends AsyncTaskLoader<List<Gateway>> {

    @Inject
    private GatewayChecker gatewayChecker;
    private List<Gateway> gatewayList;

    @Inject
    public GatewayListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Gateway> loadInBackground() {
        return gatewayChecker.fetchList();
    }

    @Override
    public void deliverResult(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;

        if (isStarted()) {
            super.deliverResult(gatewayList);
        }
    }

    @Override
    protected void onStartLoading() {
        if (this.gatewayList != null) {
            deliverResult(this.gatewayList);
        }

        if (takeContentChanged() || this.gatewayList == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Gateway> gatewayList) {
        super.onCanceled(gatewayList);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (this.gatewayList != null) {
            this.gatewayList = null;
        }
    }
}