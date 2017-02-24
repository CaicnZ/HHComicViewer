package org.huxizhijian.hhcomicviewer2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import org.huxizhijian.hhcomicviewer2.view.listener.OnCenterTapListener;
import org.huxizhijian.hhcomicviewer2.view.listener.OnLeftOrRightTapListener;

/**
 * Created by wei on 2017/1/21.
 */

public class OpenMenuFrameLayout extends FrameLayout {

    private GestureDetector mGestureDetector;

    private OnCenterTapListener onCenterTapListener;
    private OnLeftOrRightTapListener onLeftOrRightTapListener;

    public OpenMenuFrameLayout(Context context) {
        this(context, null, 0);
    }

    public OpenMenuFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenMenuFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                //单击屏幕中心开启菜单功能
                float x_up, y_up;
                x_up = event.getX();
                y_up = event.getY();
                if (onCenterTapListener.isOpen()) {
                    onCenterTapListener.closeMenu();
                    return true;
                }
                if (event.getPointerCount() == 1) {
                    if (onCenterTapListener != null &&
                            (x_up > (getWidth() / 3)) && (x_up < (getWidth() / 3 * 2))) {
                        if ((y_up > (getHeight() / 3))) {
                            //当短按屏幕中心下方时，开启menu
                            if (!onCenterTapListener.isOpen()) {
                                onCenterTapListener.openMenu();
                                return true;
                            }
                        }
                    } else if (onLeftOrRightTapListener != null && x_up < (getWidth() / 3)) {
                        //当短按屏幕左边时，显示前一页
                        onLeftOrRightTapListener.leftTap();
                    } else if (onLeftOrRightTapListener != null && x_up > (getWidth() / 3 * 2)) {
                        //当短按屏幕右边时，显示后一页
                        onLeftOrRightTapListener.rightTap();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (onCenterTapListener == null && onLeftOrRightTapListener == null)
            return false;
        return mGestureDetector.onTouchEvent(ev);
    }

    public void setOnCenterTapListener(OnCenterTapListener onCenterTapListener) {
        this.onCenterTapListener = onCenterTapListener;
    }

    public void setOnLeftOrRightTapListener(OnLeftOrRightTapListener leftOrRightTapListener) {
        this.onLeftOrRightTapListener = leftOrRightTapListener;
    }

}
