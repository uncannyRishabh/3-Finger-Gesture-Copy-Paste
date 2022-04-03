package com.uncanny.gesturecopypaste;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Objects;

public class GestureCP extends androidx.appcompat.widget.AppCompatEditText {
    private final String TAG = "3FingerCP";
    private String selectedText = "";
    private int [] pointers = new int[20]; //in case some mf puts 10 fingers at once
    private double iArea,fArea;

    public GestureCP(Context context) {
        super(context);
    }

    public GestureCP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureCP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private double Area(int x1,int y1, int x2, int y2, int x3, int y3){
        return Math.abs(.5 * (x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                performClick();
                pointers[pointerIndex] = (int) event.getX(pointerIndex);
                pointers[pointerIndex+3] = (int) event.getY(pointerIndex);

                if (event.getPointerCount() == 3) {
//                    Log.e(TAG, "onTouchEvent: DOWN");

                    iArea = Area(pointers[0]
                                ,pointers[3]
                                ,pointers[1]
                                ,pointers[4]
                                ,pointers[2]
                                ,pointers[5]);

//                    Log.e(TAG, "onTouchEvent: AREA DOWN : "+ fArea);
                }
                return super.onTouchEvent(event);
            }

            case MotionEvent.ACTION_MOVE:{
                if(event.getPointerCount()==3) {
                    for (int i = 0; i < 3; i++) {
                        pointers[i] = (int) event.getX(i);
                        pointers[i + 3] = (int) event.getY(i);

                    }

                    fArea = Area(pointers[0]
                            ,pointers[3]
                            ,pointers[1]
                            ,pointers[4]
                            ,pointers[2]
                            ,pointers[5]);

//                    Log.e(TAG, "onTouchEvent: AREA MOVE : "+ Arrays.toString(pointers));
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:{

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                if(iArea !=0 && fArea!=0){
                    if(iArea > fArea) {
                        Log.e(TAG, "onTouchEvent: COPY from if");
                        int startSelection = this.getSelectionStart();
                        int endSelection = this.getSelectionEnd();
                        if (!Objects.equals(this.getText(), null)) {
                            selectedText = this.getText().toString().substring(startSelection, endSelection);
                            ClipData clip = ClipData.newPlainText(null,selectedText);
                            clipboard.setPrimaryClip(clip);
                        }

                    }
                    if(iArea < fArea) {
                        Log.e(TAG, "onTouchEvent: PASTE from else");
                        this.append(clipboard.getPrimaryClip().getItemAt(0).getText());
                    }
                    Log.e(TAG, "onTouchEvent: "+((iArea > fArea) ? "COPY" : "PASTE"));
                    Log.e(TAG, "onTouchEvent: fArea = "+fArea+" - iarea = "+iArea+" = "+(fArea - iArea));
                }

                iArea =0; fArea=0; //RESET VARS
                return super.onTouchEvent(event);
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
