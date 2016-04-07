package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.inject.Inject;

import roboguice.service.RoboService;

public class NodeCheckerService extends RoboService {

    @Inject
    private NodeChecker nodeChecker;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.getClass().getSimpleName(), "Starting service to reload node list");
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... ignored) {
                nodeChecker.reloadList();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                stopSelf();
            }
        }.execute();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "Stopping service");
        super.onDestroy();
    }
}