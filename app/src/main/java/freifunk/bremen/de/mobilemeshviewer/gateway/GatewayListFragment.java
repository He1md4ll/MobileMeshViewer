package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.CustomListFragment;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;


public class GatewayListFragment extends CustomListFragment<Gateway> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        final ArrayAdapter<Gateway> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Lists.<Gateway>newArrayList());
        setAdapter(arrayAdapter);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_search);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Gateway gateway = (Gateway) l.getItemAtPosition(position);
        final Intent intent = new Intent(this.getActivity(), GatewayActivity.class);
        intent.putExtra(GatewayActivity.BUNDLE_GATEWAY, gateway);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayListUpdated(GatewayListUpdatedEvent ignored) {
        onReloadFinished();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGatewayListUpdatedMain(GatewayListUpdatedEvent event) {
        onReloadFinishedMain(event);
    }
}