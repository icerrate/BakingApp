package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.view.common.BaseView;

/**
 * @author Ivan Cerrate.
 */

public interface StepDetailView extends BaseView {

    void loadThumbnailSource(String thumbnailUrl);

    void showThumbnail(boolean show);

    void loadVideoSource(String videoUrl, Long videoTime, Boolean videoAutoplay);

    void showVideo(boolean show);

    void showDescription(String description);
}
