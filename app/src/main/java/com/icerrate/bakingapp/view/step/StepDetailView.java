package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.view.common.BaseView;

/**
 * @author Ivan Cerrate.
 */

public interface StepDetailView extends BaseView {

    void showShortDescription(String shortDescription);

    void loadThumbnailSource(String thumbnailUrl);

    void showThumbnail(boolean show);

    void loadVideoSource(String videoUrl);

    void showVideo(boolean show);

    void showDescription(String description);
}
