package com.icerrate.bakingapp.view.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseActivity;

public class StepDetailActivity extends BaseActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, StepDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            Step stepDetail = getIntent().getParcelableExtra(StepDetailFragment.KEY_STEP_DETAIL);
            setTitle(stepDetail.getShortDescription());
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(stepDetail);
            replaceFragment(R.id.content, stepDetailFragment);
        }
    }
}
