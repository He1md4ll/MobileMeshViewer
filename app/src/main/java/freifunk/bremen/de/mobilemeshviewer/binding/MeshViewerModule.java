package freifunk.bremen.de.mobilemeshviewer.binding;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.ListLoader;
import freifunk.bremen.de.mobilemeshviewer.MeshViewerActivity;
import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.api.manager.ConnectionManager;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayChecker;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayListFragment;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayNodeDetailLoader;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;
import freifunk.bremen.de.mobilemeshviewer.node.NodeDetailLoader;
import freifunk.bremen.de.mobilemeshviewer.node.NodeListFragment;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

@Module
public class MeshViewerModule {

    private Application application;

    public MeshViewerModule(Application application) {
        this.application = application;
    }

    @Provides
    public AlarmController providesAlarmController(AlarmManager alarmManager, Context context, PreferenceController preferenceController) {
        return new AlarmController(alarmManager, context, preferenceController);
    }

    @Provides
    @Reusable
    public ConnectionManager providesConnectionManager(ConnectivityManager connectivityManager) {
        return new ConnectionManager(connectivityManager);
    }

    @Provides
    @Reusable
    public RetrofitServiceManager providesRetrofitManager(ConnectionManager connectionManager) {
        return new RetrofitServiceManager(connectionManager);
    }

    @Provides
    public NodeDetailConverter providesNodeDetailConverter(Context context) {
        return new NodeDetailConverter(context);
    }

    @Provides
    @Singleton
    public Checkable<Gateway> providesGatewayChecker(PreferenceController preferenceController, RetrofitServiceManager retrofitServiceManager) {
        return new GatewayChecker(preferenceController, retrofitServiceManager);
    }

    @Provides
    public GatewayNodeDetailLoader providesGatewayNodeDetailLoader(Checkable<Node> nodeChecker) {
        return new GatewayNodeDetailLoader(nodeChecker);
    }

    @Provides
    @Singleton
    public Checkable<Node> providesNodeChecker(PreferenceController preferenceController, RetrofitServiceManager retrofitServiceManager) {
        return new NodeChecker(preferenceController, retrofitServiceManager);
    }

    @Provides
    public NodeDetailLoader providesNodeDetailLoader(Checkable<Node> nodeChecker) {
        return new NodeDetailLoader(nodeChecker);
    }

    @Provides
    public ListLoader<Node> providesNodeListLoader(Context context, Checkable<Node> nodeCheckable) {
        return new ListLoader<>(context, nodeCheckable);
    }

    @Provides
    public ListLoader<Gateway> providesGatewayListLoader(Context context, Checkable<Gateway> gatewayCheckable) {
        return new ListLoader<>(context, gatewayCheckable);
    }

    @Provides
    @Reusable
    public PreferenceController providesPreferenceController(SharedPreferences sharedPreferences) {
        return new PreferenceController(sharedPreferences);
    }

    @Provides
    public MeshViewerActivity providesMeshViewerActivity() {
        return new MeshViewerActivity();
    }

    @Provides
    public NodeListFragment providesNodeListFragment() {
        return new NodeListFragment();
    }

    @Provides
    public GatewayListFragment providesGatewayListFragment() {
        return new GatewayListFragment();
    }

    @Provides
    @Singleton
    public Context context() {
        return application;
    }

    @Provides
    @Singleton
    public AlarmManager providesAlarmManager() {
        return (AlarmManager) application
                .getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    @Singleton
    public ConnectivityManager providesConnectivityManager() {
        return (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    public NotificationManager providesNotificationManager() {
        return (NotificationManager) application
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}