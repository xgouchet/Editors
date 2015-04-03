package fr.xgouchet.xmleditor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.xgouchet.xmleditor.core.utils.DoubleDeckerBus;
import fr.xgouchet.xmleditor.ui.activities.EditorActivity;

/**
 * @author Xavier Gouchet
 */
@Module(
//       includes = { FrenchModule.class,},
        injects = {EditorActivity.class}
)
public class MainModule {

    @Provides
    @Singleton
    DoubleDeckerBus provideEventBus() {
        return new DoubleDeckerBus();
    }


}
