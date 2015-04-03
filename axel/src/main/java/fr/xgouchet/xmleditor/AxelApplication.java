package fr.xgouchet.xmleditor;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @author Xavier Gouchet
 */
public class AxelApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        // load the Dagger Object graph
        mObjectGraph = ObjectGraph.create(getModules().toArray());
    }

    /**
     * A list of modules to use for the application graph.
     * <p/>
     * Subclasses can override this method to provide additional modules provided they call
     * {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new MainModule());
    }

    /**
     * Injects the given object with the proper dependencies
     *
     * @param object the object to inject
     */
    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

}
