package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BasePresenter;

/**
 * @author Ivan Cerrate.
 */

public class StepDetailPresenter extends BasePresenter<StepDetailView> {

    private Step stepDetail;

    private Long videoTime = 3L;

    private Boolean videoAutoplay = false;

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
            view.showThumbnail(true);
            view.loadThumbnailSource(thumbnailUrl);
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            view.showVideo(true);
            view.loadVideoSource(videoUrl, videoTime, videoAutoplay);
        }
    }

    private void showDescription(String description) {
        if (description != null && !description.isEmpty()) {
            view.showDescription(description);
        } else {
            view.showDescription(getStringRes(R.string.description_no_data));
        }
    }

    public boolean containsVideo() {
        return stepDetail.getVideoURL() != null && !stepDetail.getVideoURL().isEmpty();
    }

    public void setStepDetail(Step stepDetail) {
        this.stepDetail = stepDetail;
    }

    public Step getStepDetail() {
        return stepDetail;
    }
    public Long getVideoTime() {
        return videoTime;
    }
    public Boolean getVideoAutoplay() {
        return videoAutoplay;
    }

    public void loadPresenterState(Step stepDetail, Long videoTime, Boolean videoAutoplay) {
        this.stepDetail = stepDetail;
        this.videoTime = videoTime;
        this.videoAutoplay = videoAutoplay;
    }
}
