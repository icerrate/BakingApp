package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.view.common.BaseCallback;
import com.icerrate.bakingapp.view.common.BasePresenter;

/**
 * @author Ivan Cerrate.
 */

public class StepDetailPresenter extends BasePresenter<StepDetailView> {

    private Integer recipeId;

    private Integer selectedStep;

    private Step stepDetail;

    private long videoTime = 0;

    private boolean videoAutoPlay = true;

    private BakingAppRepository recipeRepository;

    public StepDetailPresenter(StepDetailView view, BakingAppRepository recipeRepository) {
        super(view);
        this.recipeRepository = recipeRepository;
    }

    public void loadStepDetail() {
        if (stepDetail == null) {
            getInternalStepDetail(recipeId, selectedStep);
        } else {
            showStepDetail(stepDetail);
        }
    }

    private void getInternalStepDetail(Integer recipeId, Integer selectedStep) {
        view.showProgressBar(true);
        recipeRepository.getStepDetail(recipeId, selectedStep, new BaseCallback<Step>() {
            @Override
            public void onSuccess(Step response) {
                stepDetail = response;
                showStepDetail(response);
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showProgressBar(false);
            }
        });
    }

    private void showStepDetail(Step stepDetail) {
        //Short Description
        view.showShortDescription(stepDetail.getShortDescription());
        //Media
        String thumbnailUrl = stepDetail.getThumbnailURL();
        String videoUrl = stepDetail.getVideoURL();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            view.showThumbnail(true);
            view.loadThumbnailSource(thumbnailUrl);
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            view.showVideo(true);
            view.loadVideoSource(videoUrl, videoTime, videoAutoPlay);
        }
        //Description
        String description = stepDetail.getDescription();
        if (description != null && !description.isEmpty()) {
            view.showDescription(description);
        } else {
            view.showDescription(getStringRes(R.string.description_no_data));
        }
    }

    public boolean containsVideo() {
        return stepDetail.getVideoURL() != null && !stepDetail.getVideoURL().isEmpty();
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getSelectedStep() {
        return selectedStep;
    }

    public void setSelectedStep(Integer selectedStep) {
        this.selectedStep = selectedStep;
    }

    public Step getStepDetail() {
        return stepDetail;
    }

    public void setStepDetail(Step stepDetail) {
        this.stepDetail = stepDetail;
    }

    public Long getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Long videoTime) {
        this.videoTime = videoTime;
    }

    public Boolean getVideoAutoPlay() {
        return videoAutoPlay;
    }

    public void setVideoAutoPlay(Boolean videoAutoPlay) {
        this.videoAutoPlay = videoAutoPlay;
    }

    public void loadPresenterState(Integer recipeId, Integer selectedStep, Step stepDetail, Long videoTime, Boolean videoAutoPLay) {
        this.recipeId = recipeId;
        this.selectedStep = selectedStep;
        this.stepDetail = stepDetail;
        this.videoTime = videoTime;
        this.videoAutoPlay = videoAutoPLay;
    }
}
