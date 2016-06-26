package de.tum.whatsappplus;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ClickInterceptorOverlayFragment extends Fragment{

    private static final String TAG = ClickInterceptorOverlayFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_click_interceptor, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = findViewAt(((ViewGroup)getView().getRootView()), (int) event.getX(), (int) event.getY());
                if (view != null) {
                    String viewId = null;
                    try {
                        viewId = getResources().getResourceName(view.getId());
                        viewId = viewId.substring(viewId.lastIndexOf('/') + 1);
                    } catch (Resources.NotFoundException e) {/* just don't display it then */}
                    Log.i(Constants.TAG_CLICK_COUNTER, "Click (view type=" + view.getClass().getSimpleName() + (viewId != null ? " id=" + viewId : "") + ")");
                } else
                    Log.i(Constants.TAG_CLICK_COUNTER, "Click");
                return false;
            }
        });
        return view;
    }

    private View findViewAt(ViewGroup viewGroup, int x, int y) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int[] location = new int[2];
            child.getLocationOnScreen(location);
            Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(), location[1] + child.getHeight());
            if (rect.contains(x, y)) {
                if (child instanceof ViewGroup)
                    return findViewAt((ViewGroup) child, x, y);
                else
                    return child;
            }
        }
        return viewGroup;
    }
}
