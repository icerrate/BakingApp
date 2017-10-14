package com.icerrate.bakingapp.view.step;

import com.icerrate.bakingapp.view.common.BaseView;

/**
 * @author Ivan Cerrate.
 */

public interface StepDetailView extends BaseView {

    void showThumbnail(String thumbnailUrl);

    void hideThumbnail();

    void showVideo(String videoUrl);

    void hideVideo();

    void showDescription(String description);
}
