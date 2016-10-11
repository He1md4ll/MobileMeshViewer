package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailNotFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.IpStatus;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

public class GatewayActivity extends AppCompatActivity {

    public static final String BUNDLE_GATEWAY = "gateway";
    @BindView(R.id.progress_container)
    View progressIndicator;
    @BindView(R.id.gateway_content)
    View gatewayContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gateway_status_table)
    TableLayout tableLayout;
    @BindView(R.id.gateway_traffic)
    TextView gatewayTraffic;
    @BindView(R.id.gateway_firmware)
    TextView gatewayFirmware;
    @BindView(R.id.gateway_uptime)
    TextView gatewayUptime;
    @BindView(R.id.gateway_loadavg)
    TextView gatewayLoadavg;
    @BindView(R.id.gateway_install_date)
    TextView gatewayInstallDate;
    @Inject
    GatewayNodeDetailLoader gatewayNodeDetailLoader;
    @Inject
    NodeDetailConverter nodeDetailConverter;
    private Gateway gateway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway);
        ((MeshViewerApp) getApplication()).getMeshViewerComponent().inject(this);
        ButterKnife.bind(this);
        gateway = getIntent().getExtras().getParcelable(BUNDLE_GATEWAY);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(gateway.getName());

        createTable();
        gatewayNodeDetailLoader.execute(gateway.getName());
    }

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

    private void createTable() {
        tableLayout.addView(createRow("UpLink", gateway.getUplink()));
        tableLayout.addView(createRow("Address", gateway.getAddresses()));
        tableLayout.addView(createRow("DNS", gateway.getDns()));
        tableLayout.addView(createRow("NTP", gateway.getNtp()));
    }

    private TableRow createRow(String label, IpStatus status) {
        final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.template_gateway_table_row, null);
        final TextView tableRowLabel = (TextView) tableRow.findViewById(R.id.table_row_label);
        tableRowLabel.setText(label);

        final ImageView tableRowStatus1 = (ImageView) tableRow.findViewById(R.id.table_row_status_1);
        final ImageView tableRowStatus2 = (ImageView) tableRow.findViewById(R.id.table_row_status_2);
        setStatus(status, tableRowStatus1, tableRowStatus2);

        return tableRow;
    }

    private void setStatus(IpStatus status, ImageView imageIpv4, ImageView imageIpv6) {
        switch (status) {
            case BOTH:
                imageIpv4.setImageResource(R.drawable.ic_online);
                imageIpv6.setImageResource(R.drawable.ic_online);
                break;
            case IPv4:
                imageIpv4.setImageResource(R.drawable.ic_online);
                imageIpv6.setImageResource(R.drawable.ic_offline);
                break;
            case IPv6:
                imageIpv4.setImageResource(R.drawable.ic_offline);
                imageIpv6.setImageResource(R.drawable.ic_online);
                break;
            default:
                imageIpv4.setImageResource(R.drawable.ic_offline);
                imageIpv6.setImageResource(R.drawable.ic_offline);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayNodeDetailFound(NodeDetailFoundEvent event) {
        final NodeDetail gatewayDetail = event.getNode();
        gatewayFirmware.setText(getString(R.string.separator,
                gatewayDetail.getNodeinfo().getSoftware().getFirmware().getRelease(),
                gatewayDetail.getNodeinfo().getSoftware().getFirmware().getBase()));
        gatewayInstallDate.setText(nodeDetailConverter.convertDate(gatewayDetail.getFirstseen()));
        if (gatewayDetail.getFlagsNode().getOnline()) {
            gatewayTraffic.setText(nodeDetailConverter.convertTraffic(gatewayDetail.getStatistics().getTraffic()));
            gatewayUptime.setText(nodeDetailConverter.convertUptime(gatewayDetail.getStatistics().getUptime()));
            gatewayLoadavg.setText(getString(R.string.traffic_separator,
                    gatewayDetail.getStatistics().getLoadavg(),
                    Math.round(gatewayDetail.getStatistics().getMemoryUsage() * 100), "%"));
        }
        progressIndicator.setVisibility(View.GONE);
        gatewayContent.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayNodeDetailFound(NodeDetailNotFoundEvent ignored) {
        progressIndicator.setVisibility(View.GONE);
        Snackbar.make(tableLayout, "Couldn't load details for " + gateway.getName(), Snackbar.LENGTH_LONG).show();
    }
}