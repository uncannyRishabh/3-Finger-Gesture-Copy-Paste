package com.uncanny.gesturecopypaste;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


public class GestureCP extends GestureDecoder {
    public GestureCP(Context context) {
        super(context);
    }

    public GestureCP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureCP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
