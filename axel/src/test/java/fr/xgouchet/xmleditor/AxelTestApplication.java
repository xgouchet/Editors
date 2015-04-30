package fr.xgouchet.xmleditor;

import android.app.Application;

import org.robolectric.Robolectric;
import org.robolectric.TestLifecycleApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Scheduler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @author Xavier Gouchet
 */
public class AxelTestApplication extends AxelApplication implements TestLifecycleApplication {

    @Override
    public void beforeTest(Method method) {
        ShadowLog.stream = System.out;
        Robolectric.getForegroundThreadScheduler().reset();
    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }

//    @Override
//    protected List<Object> getModules() {
//        return Arrays.<Object>asList(
//                new TestKamodulox(),
//                new TestFrenchModule());
//    }
}
