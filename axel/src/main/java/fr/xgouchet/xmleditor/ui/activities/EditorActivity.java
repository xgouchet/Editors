package fr.xgouchet.xmleditor.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.xmleditor.AxelApplication;
import fr.xgouchet.xmleditor.R;
import fr.xgouchet.xmleditor.core.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class EditorActivity
        extends FragmentActivity
        implements Toolbar.OnMenuItemClickListener {

    public static final String TAG = EditorActivity.class.getSimpleName();

    @Inject
    DoubleDeckerBus mBus;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    //////////////////////////////////////////////////////////////////////////////////////
    // Activity Lifecycle
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject dependencies
        ((AxelApplication) getApplication()).inject(this);

        // Set content views
        setContentView(R.layout.activity_editor);

        // Inject ButterKnife views
        ButterKnife.inject(this);

        // setup activity
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.editor_title_empty);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Toolbar MenuItemClickListener
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
}
