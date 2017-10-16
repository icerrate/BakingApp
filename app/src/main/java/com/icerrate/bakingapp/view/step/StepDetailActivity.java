package com.icerrate.bakingapp.view.step;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseActivity;

public class StepDetailActivity extends BaseActivity {

    private Step stepDetail;

    public static Intent makeIntent(Context context) {
        return new Intent(context, StepDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            stepDetail = getIntent().getParcelableExtra(StepDetailFragment.KEY_STEP_DETAIL);
            setTitle(stepDetail.getShortDescription());
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(stepDetail);
            replaceFragment(R.id.content, stepDetailFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(StepDetailFragment.KEY_STEP_DETAIL, stepDetail);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepDetail = savedInstanceState.getParcelable(StepDetailFragment.KEY_STEP_DETAIL);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        stepDetailFragment.onConfigurationChanged();
    }
}
