package com.uncanny.gesturecopypaste;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings({"FieldMayBeFinal",
        "FieldCanBeLocal"})
public abstract class GestureDecoder extends androidx.appcompat.widget.AppCompatEditText{
    private final String TAG = "3FingerCP";
    private final int [] Ipointers = new int[20]; //in case some mf puts 10 fingers at once
    private final int [] Fpointers = new int[20]; //in case some mf puts 10 fingers at once
    private double iArea,fArea;
    private boolean gestureDetected = false;
    private String PREV_ACTION = "";
    private UncannyToast uToast;

    public GestureDecoder(@NonNull Context context) {
        super(context);
        init();
    }

    public GestureDecoder(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureDecoder(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Util.initClipboardManager(getContext());
        uToast = new UncannyToast();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return Gesture(event);
    }

    private boolean Gesture(MotionEvent event){
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int startSelection=this.getSelectionStart();
        int endSelection=this.getSelectionEnd();

        switch (action) {
            case MotionEvent.ACTION_DOWN:{
                Log.e(TAG, "Gesture: GD = FALSE .");
                PREV_ACTION = "";
                gestureDetected = false;
                break;

            }
            case MotionEvent.ACTION_POINTER_DOWN: {

                Ipointers[pointerIndex] = (int) event.getX(pointerIndex);
                Ipointers[pointerIndex+3] = (int) event.getY(pointerIndex);

                if (event.getPointerCount() == 3) {
                    iArea = Util.Area(Ipointers[0], Ipointers[3], Ipointers[1], Ipointers[4], Ipointers[2], Ipointers[5]);
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:{
//                Log.e(TAG, "Gesture: MOVED");
                if(event.getPointerCount()==3) {
                    for (int i = 0; i < 3; i++) {
                        Fpointers[i] = (int) event.getX(i);
                        Fpointers[i + 3] = (int) event.getY(i);
                    }
                    fArea = Util.Area(Fpointers[0], Fpointers[3], Fpointers[1], Fpointers[4], Fpointers[2], Fpointers[5]);
                    return true;
                }
//                Log.e(TAG, "Gesture ? "+gestureDetected+" POINTER COUNT : "+event.getPointerCount());
                return gestureDetected && event.getPointerCount() > 1 || super.onTouchEvent(event);
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:{
                Log.e(TAG, "onTouchEvent: ACTION_POINTER_UP : " + event.getPointerCount());

                if(event.getPointerCount()==3){
                    gestureDetected = true;

                    Log.e(TAG, "Gesture: "+Ipointers[0]+"  "+Fpointers[0]
                            +"  "+Ipointers[1]+"  "+Fpointers[1]
                            +"  "+Ipointers[2]+"  "+Fpointers[2]+" redo : "+(Ipointers[0] >= Fpointers[0] &&
                            Ipointers[1] > Fpointers[1] &&
                            Ipointers[2] > Fpointers[2]));

//                    if(Ipointers[1] < Fpointers[1] &&
//                       Ipointers[2] < Fpointers[2]){
//                        PREV_ACTION = "UNDO";
//                        Log.e(TAG, "Gesture: UNDO");
//                        break;
//                    }
//                    else if(Ipointers[1] > Fpointers[1] &&
//                            Ipointers[2] > Fpointers[2]){
//                        PREV_ACTION = "REDO";
//                        Log.e(TAG, "Gesture: REDO");
//                        break;
//                    }

                    if (iArea != 0 && fArea != 0) {
//                    gestureDetected = true;
                        if (iArea > fArea) {
                            PREV_ACTION = "COPY";
                            Log.e(TAG, "onTouchEvent: COPY");
                            Util.performCopy(this.getText().toString()
                                    , this.getSelectionStart(), this.getSelectionEnd(), getContext());
                            uToast.make(this.getRootView(),getContext(),"Copied",2000);
                        } else if (iArea < fArea) {
                            PREV_ACTION = "PASTE";
                            Log.e(TAG, "onTouchEvent: PASTE");
                            this.setText(Util.performPaste(this.getText().toString()
                                    , this.getSelectionStart(), this.getSelectionEnd(), getContext()));
                            uToast.make(this.getRootView(),getContext(),"Pasted",2000);
                        }
                    }
//                    Log.e(TAG, "onTouchEvent: "+((iArea > fArea) ? "COPY" : "PASTE"));
//                    Log.e(TAG, "onTouchEvent: fArea = "+fArea+" - iarea = "+iArea+" = "+(fArea - iArea));
                    iArea = 0;
                    fArea = 0; //RESET VARS
                    break;
                }
            }
            case MotionEvent.ACTION_UP:{
//                Log.e(TAG, "onTouchEvent: ACTION_UP : " + event.getPointerCount() +" gd ? "+gestureDetected);
                Log.e(TAG, "Gesture:"+this.getPrivateImeOptions());
                if(event.getPointerCount()==1) {
                    Log.e(TAG, "Gesture: RESETTING SELECTION "+PREV_ACTION);
                    resetSelection();
                    break;
//                    return gestureDetected || super.onTouchEvent(event);
                }

            }

        }

        performClick();
//        Log.e(TAG, "Gesture: FINAL RETURN EXECUTES 😡 | gd : "+gestureDetected);
        return gestureDetected || super.onTouchEvent(event);
    }

    private void resetSelection(){
//        this.setSelection(3);
        Log.e(TAG, "Gesture: RESETTING SELECTION "+PREV_ACTION+" getselection = "+this.getSelectionStart());
    }

}