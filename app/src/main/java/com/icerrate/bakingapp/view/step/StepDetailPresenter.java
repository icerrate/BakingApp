package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BasePresenter;

/**
 * @author Ivan Cerrate.
 */

public class StepDetailPresenter extends BasePresenter<StepDetailView> {

    private Step stepDetail;

    public StepDetailPresenter(StepDetailView view) {
        super(view);
    }

    public void loadStepDetail() {
        if (stepDetail != null) {
            showMediaResource(stepDetail.getThumbnailURL(), stepDetail.getVideoURL());
            showDescription(stepDetail.getDescription());
        }
    }

    private void showMediaResource(String thumbnailUrl, String videoUrl) {
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            view.hideVideo();
            view.showThumbnail(thumbnailUrl);
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            view.hideThumbnail();
            view.showVideo(videoUrl);
        } else {
            view.hideThumbnail();
            view.hideVideo();
        }
    }

    private void showDescription(String description) {
        if (description != null && !description.isEmpty()) {
            view.showDescription(description);
        } else {
            view.showDescription(getStringRes(R.string.description_no_data));
        }
    }

    public void setStepDetail(Step stepDetail) {
        this.stepDetail = stepDetail;
    }

    public Step getStepDetail() {
        return stepDetail;
    }

    public void loadPresenterState(Step stepDetail) {
        this.stepDetail = stepDetail;
    }
}
