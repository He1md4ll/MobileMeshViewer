package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import roboguice.fragment.provided.RoboListFragment;


public class GatewayListFragment extends RoboListFragment implements LoaderManager.LoaderCallbacks<List<Gateway>> {

    @Inject
    private GatewayListLoader gatewayListLoader;
    private ArrayAdapter<Gateway> adapter;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(android.R.layout.list_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setEmptyText(getActivity().getString(R.string.list_no_gateways));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Gateway>());
        setListAdapter(adapter);
        setListShown(false);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_search);
    }

    @Override
    public Loader<List<Gateway>> onCreateLoader(int id, Bundle args) {
        gatewayListLoader.reset();
        return gatewayListLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Gateway>> loader, List<Gateway> data) {
        adapter.clear();
        adapter.addAll(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Gateway>> loader) {
        adapter.clear();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGatewayListUpdated(GatewayListUpdatedEvent event) {
        gatewayListLoader.onContentChanged();
    }
}