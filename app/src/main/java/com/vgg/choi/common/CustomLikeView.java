package com.vgg.choi.common;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookException;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.Utility;
import com.facebook.share.internal.LikeActionController;
import com.facebook.share.widget.LikeView;

/**
 * Created by LeHai on 3/10/2017.
 */

public class CustomLikeView {
    private LikeView.Style likeViewStyle = LikeView.Style.STANDARD;
    private LikeView.HorizontalAlignment horizontalAlignment = LikeView.HorizontalAlignment.CENTER;
    private LikeView.AuxiliaryViewPosition auxiliaryViewPosition = LikeView.AuxiliaryViewPosition.BOTTOM;
    private LikeView.ObjectType objectType = LikeView.ObjectType.OPEN_GRAPH;

    private LikeActionControllerCreationCallback creationCallback;
    private LikeActionController likeActionController;
    private String objectId;

    public void init(String newObjectId, LikeView.ObjectType newObjectType) {
        creationCallback = new LikeActionControllerCreationCallback();
        this.objectId = newObjectId;
        this.objectType = newObjectType;
        LikeActionController.getControllerForObjectId(
                newObjectId,
                newObjectType,
                creationCallback);
    }
    public void toggleLike(Activity activity) {
        if (likeActionController != null) {
            likeActionController.toggleLike(
                    activity,
                    null,
                    getAnalyticsParameters());
        }
    }
    private void associateWithLikeActionController(LikeActionController likeActionController) {
        this.likeActionController = likeActionController;
    }
    private Bundle getAnalyticsParameters() {
        Bundle params = new Bundle();
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_STYLE, likeViewStyle.toString());
        params.putString(
                AnalyticsEvents.PARAMETER_LIKE_VIEW_AUXILIARY_POSITION,
                auxiliaryViewPosition.toString());
        params.putString(
                AnalyticsEvents.PARAMETER_LIKE_VIEW_HORIZONTAL_ALIGNMENT,
                horizontalAlignment.toString());
        params.putString(
                AnalyticsEvents.PARAMETER_LIKE_VIEW_OBJECT_ID,
                Utility.coerceValueIfNullOrEmpty(objectId, ""));
        params.putString(
                AnalyticsEvents.PARAMETER_LIKE_VIEW_OBJECT_TYPE,
                objectType.toString());

        return params;
    }

    private class LikeActionControllerCreationCallback
            implements LikeActionController.CreationCallback {
        private boolean isCancelled;

        public void cancel() {
            isCancelled = true;
        }

        @Override
        public void onComplete(
                LikeActionController likeActionController,
                FacebookException error) {
            Log.d("LikeView", "Controller");
            if (isCancelled) {
                return;
            }

            if (likeActionController != null) {
                if (!likeActionController.shouldEnableView()) {
                    error = new FacebookException(
                            "Cannot use LikeView. The device may not be supported.");
                }

                // Always associate with the controller, so it can get updates if the view gets
                // enabled again.
                associateWithLikeActionController(likeActionController);
                //updateLikeStateAndLayout();
            }

            if (error != null) {
            /*    if (onErrorListener != null) {
                    onErrorListener.onError(error);
                }
            }*/

                //LikeView.this.creationCallback = null;
            }
        }
    }
}
