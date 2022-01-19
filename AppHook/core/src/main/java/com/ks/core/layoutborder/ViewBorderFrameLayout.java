package com.ks.core.layoutborder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.ks.core.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglikun on 2019/1/12
 */
public class ViewBorderFrameLayout extends FrameLayout {

    private static final String TAG = "ViewBorderFrameLayout";

    public ViewBorderFrameLayout(@NonNull Context context) {
        super(context);
        setId(R.id.dokit_view_border_id);
        //LogHelper.i(TAG, "childId=====>" + UIUtils.getIdText(this));
    }

    public ViewBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setId(R.id.dokit_view_border_id);
    }

    public ViewBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setId(R.id.dokit_view_border_id);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (LayoutBorderConfig.isLayoutBorderOpen()) {
            traverseChild(this);
        } else {
            clearChild(this);
        }
    }

    private void traverseChild(View view) {
        //过滤掉dokitView
        if (view instanceof ViewGroup) {
            replaceDrawable(view);
            int childCount = ((ViewGroup) view).getChildCount();
            if (childCount != 0) {
                for (int index = 0; index < childCount; index++) {
                    traverseChild(((ViewGroup) view).getChildAt(index));
                }
            }
        } else {
            replaceDrawable(view);
        }
    }

    private void replaceDrawable(View view) {
        if (view instanceof TextureView) {
            // 过滤TextureView
            return;
        }
        MyLayerDrawable newDrawable;
        if (view.getBackground() != null) {
            Drawable oldDrawable = view.getBackground();
            if (oldDrawable instanceof MyLayerDrawable) {
                for (int i = 0; i < ((MyLayerDrawable) oldDrawable).getNumberOfLayers(); i++) {
                    if (((LayerDrawable) oldDrawable).getDrawable(i) instanceof ViewBorderDrawable) {
                        // already replace
                        return;
                    }
                }
                newDrawable = new MyLayerDrawable(new Drawable[]{
                        oldDrawable,
                        new ViewBorderDrawable(view)
                });
            } else {
                newDrawable = new MyLayerDrawable(new Drawable[]{
                        oldDrawable,
                        new ViewBorderDrawable(view)
                });
            }
        } else {
            newDrawable = new MyLayerDrawable(new Drawable[]{
                    new ViewBorderDrawable(view)
            });
        }
        try {
            view.setBackground(newDrawable);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    private void clearChild(View view) {
        if (view instanceof ViewGroup) {
            clearDrawable(view);
            int childCount = ((ViewGroup) view).getChildCount();
            if (childCount != 0) {
                for (int index = 0; index < childCount; index++) {
                    clearChild(((ViewGroup) view).getChildAt(index));
                }
            }
        } else {
            clearDrawable(view);
        }
    }

    private void clearDrawable(View view) {
        if (view.getBackground() == null) {
            return;
        }
        Drawable oldDrawable = view.getBackground();
        if (!(oldDrawable instanceof MyLayerDrawable)) {
            return;
        }
        MyLayerDrawable layerDrawable = (MyLayerDrawable) oldDrawable;
        List<Drawable> drawables = new ArrayList<>();
        for (int i = 0; i < layerDrawable.getNumberOfLayers(); i++) {
            if (layerDrawable.getDrawable(i) instanceof ViewBorderDrawable) {
                continue;
            }
            drawables.add(layerDrawable.getDrawable(i));
        }
        MyLayerDrawable newDrawable = new MyLayerDrawable(drawables.toArray(new Drawable[drawables.size()]));
        view.setBackground(newDrawable);
    }
}