package com.icerrate.bakingapp.view.common;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.icerrate.bakingapp.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Ivan Cerrate.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseFragmentListener {

    private Unbinder unbinder;
    protected Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void setNavigationToolbar(boolean navigation) {
        if (navigation && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void replaceFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setToolbarVisibility(int visibility) {
        if (toolbar != null) {
            toolbar.setVisibility(visibility);
        }
    }
}
