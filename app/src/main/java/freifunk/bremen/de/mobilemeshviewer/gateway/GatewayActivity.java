package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_gateway)
public class GatewayActivity extends RoboAppCompatActivity {

    public static final String BUNDLE_GATEWAY = "gateway";

    @InjectExtra(value = BUNDLE_GATEWAY)
    private Gateway gateway;
    @InjectView(R.id.gateway_name)
    private TextView gatewayName;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gatewayName.setText(gateway.getName());
    }
}