package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import freifunk.bremen.de.mobilemeshviewer.ListFragment;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;


public class GatewayListFragment extends ListFragment<Gateway> {

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
}