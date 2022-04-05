package com.uncanny.gesturecopypaste;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.logging.Handler;

@SuppressWarnings({"FieldMayBeFinal",
        "FieldCanBeLocal"})
public class UncannyToast {
    private RelativeLayout toast;
    private TextView v;
    private ObjectAnimator animator;
    private Runnable hideToast = new Runnable() {
        @Override
        public void run() {
//            animator = ObjectAnimator.ofFloat(toast, "scaleX", 1f,.8f);
//            animator.setInterpolator(new BounceInterpolator());
//            animator.setDuration(500);
//            animator.start();
//            animator = ObjectAnimator.ofFloat(toast, "scaleY", 1f,.8f);
//            animator.setInterpolator(new BounceInterpolator());
//            animator.setDuration(550);
//            animator.start();
//            animator = ObjectAnimator.ofFloat(toast, "alpha", 1f,.7f);
//            animator.setDuration(550);
//            animator.start();

//            ScaleAnimation expanding = new ScaleAnimation(
//                    1.0f, 0f,
//                    1.0f, 0f,
//                    Animation.RELATIVE_TO_PARENT, 0,
//                    Animation.RELATIVE_TO_PARENT, 0);
//            expanding.setDuration(250);
//            toast.startAnimation(expanding);
            animator = ObjectAnimator.ofFloat(v, "translationX", 0f,30f);
            animator.setInterpolator(new BounceInterpolator());
            animator.setDuration(650);
            animator.start();

            animator = ObjectAnimator.ofFloat(v, "alpha", 1f,.3f);
            animator.setInterpolator(new BounceInterpolator());
            animator.setDuration(500);
            animator.start();
            toast.postDelayed(() -> toast.setVisibility(View.INVISIBLE),400);
        }
    };

    private Runnable showToast = new Runnable() {
        @Override
        public void run() {
            toast.setVisibility(View.VISIBLE);
            toast.setAlpha(1f);

            animator = ObjectAnimator.ofFloat(v, "alpha", .7f,1f);
            animator.setInterpolator(new BounceInterpolator());
            animator.setDuration(650);
            animator.start();

            animator = ObjectAnimator.ofFloat(v, "translationX", 30f,0f);
            animator.setInterpolator(new BounceInterpolator());
            animator.setDuration(650);
            animator.start();

//            animator = ObjectAnimator.ofFloat(toast, "scaleX", .7f,1f);
//            animator.setInterpolator(new BounceInterpolator());
//            animator.setDuration(500);
//            animator.start();
//            animator = ObjectAnimator.ofFloat(toast, "scaleY", .7f,1f);
//            animator.setInterpolator(new BounceInterpolator());
//            animator.setDuration(500);
//            animator.start();
        }
    };

    public UncannyToast(){}

    public void make(View view, Context context, CharSequence text, int duration) {
        view.removeCallbacks(hideToast);

        if(view.findViewById(R.id.Toast)==null){
            LayoutInflater.from(context).inflate(
                    R.layout.uncanny_toast, (ViewGroup) view, true);
        }

        toast = view.findViewById(R.id.Toast);
        toast.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        toast.findViewById(R.id.toast_icon).setClipToOutline(true);

        v = view.findViewById(R.id.toast_text);
        v.setText(text);
        view.post(showToast);

        //set margins
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,48,0,0);
//        toast.setLayoutParams(lp);

        view.postDelayed(hideToast,duration);
    }

}
