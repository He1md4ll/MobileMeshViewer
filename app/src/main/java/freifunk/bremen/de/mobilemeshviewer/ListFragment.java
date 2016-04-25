package freifunk.bremen.de.mobilemeshviewer;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.event.ReloadFinishedEvent;

public abstract class ListFragment<T> extends SwipeRefreshListRoboFragment implements LoaderManager.LoaderCallbacks<List<T>> {

    @Inject
    private ListLoader<T> gatewayListLoader;
    @Inject
    private AlarmController alarmController;
    private ArrayAdapter<T> adapter;
    private boolean visible;
    private Optional<Snackbar> snackbarOptional = Optional.absent();

    public ArrayAdapter<T> getAdapter() {
        return adapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        gatewayListLoader.onContentChanged();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setEmptyText(getActivity().getString(R.string.list_empty));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<T>());
        setListAdapter(adapter);
        setListShown(false);

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alarmController.sendAlarmImmediately();
                if (snackbarOptional.isPresent()) {
                    snackbarOptional.get().dismiss();
                }
            }
        });

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visible = isVisibleToUser;
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        gatewayListLoader.reset();
        return gatewayListLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        adapter.clear();
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        adapter.clear();
    }

    public void onReloadFinished() {
        gatewayListLoader.onContentChanged();
    }

    public void onReloadFinishedMain(ReloadFinishedEvent event) {
        // Hide refresh progress indicator from SwipeRefreshLayout
        if (isRefreshing()) {
            setRefreshing(false);
        }

        // Hide refresh progress indicator from ListView
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

        // Inform user with SnackBar
        if (visible && (!snackbarOptional.isPresent() || !snackbarOptional.get().isShown())) {
            final Snackbar snackbar;
            if (event.isSuccess()) {
                snackbar = Snackbar.make(getListView(), R.string.snackbar_success, Snackbar.LENGTH_SHORT);
            } else {
                snackbar = Snackbar.make(getListView(), R.string.snackbar_fail, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.snackbar_button_OK, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            snackbar.show();
            snackbarOptional = Optional.of(snackbar);
        }
    }
}