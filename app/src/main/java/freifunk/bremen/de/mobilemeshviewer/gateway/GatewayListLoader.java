package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayListLoader extends AsyncTaskLoader<List<Gateway>> {

    @Inject
    private GatewayController gatewayController;
    private List<Gateway> gatewayList;

    @Inject
    public GatewayListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Gateway> loadInBackground() {
        return gatewayController.getGatewayList();
    }

    @Override
    public void deliverResult(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;

        if (isStarted() && !this.gatewayList.isEmpty()) {
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