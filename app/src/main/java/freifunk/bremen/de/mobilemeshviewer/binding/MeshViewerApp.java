package freifunk.bremen.de.mobilemeshviewer.binding;

import android.app.Application;

public class MeshViewerApp extends Application {

    private MeshViewerComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMeshViewerComponent
                .builder()
                .meshViewerModule(new MeshViewerModule(this))
                .build();
    }

    public MeshViewerComponent getMeshViewerComponent() {
        return component;
    }

    public void setComponent(MeshViewerComponent component) {
        this.component = component;
    }
}
