package com.blogspot.huyhungdinh.tv.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class ExGridView extends GridView {


    boolean expanded = false;

    public ExGridView(Context context) {
        super(context);
    }

    public ExGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExGridView(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
