package freifunk.bremen.de.mobilemeshviewer;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.event.ReloadFinishedEvent;

public abstract class CustomListFragment<T> extends SwipeRefreshListRoboFragment implements LoaderManager.LoaderCallbacks<List<T>> {

    @Inject
    private ListLoader<T> listLoader;
    @Inject
    private AlarmController alarmController;
    private CustomArrayAdapter<T> adapter;
    private boolean visible;
    private Optional<Snackbar> snackbarOptional = Optional.absent();

    public CustomArrayAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(CustomArrayAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        listLoader.onContentChanged();
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
        listLoader.reset();
        return listLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        final int index = getListView().getFirstVisiblePosition();
        final View v = getListView().getChildAt(0);
        final int top = (v == null) ? 0 : (v.getTop() - getListView().getPaddingTop());
        adapter.clear();
        adapter.addAll(data);
        getListView().setSelectionFromTop(index, top);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        adapter.clear();
    }

    public void onReloadFinished() {
        listLoader.onContentChanged();
    }

    public void onReloadFinishedMain(ReloadFinishedEvent event) {
        // Hide refresh progress indicator from SwipeRefreshLayout
        if (isRefreshing()) {
            setRefreshing(false);
        }

        // Hide refresh progress indicator from ListView
        if (!getListView().isShown()) {
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
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