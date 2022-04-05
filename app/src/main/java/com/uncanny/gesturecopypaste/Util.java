package com.uncanny.gesturecopypaste;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Util {
    private static ClipData clip;
    private static ClipboardManager clipboardManager;
    private Context context;

    private int SELECTION_START;
    private int SELECTION_END;

    private static String copiedText;
    private static String pasteText;

    public void Util(){

    }

    public void Util(Context context){
        this.context = context;
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public static void initClipboardManager(Context context){
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private static void setClipboardManager(Context context){
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static double Area(int x1,int y1, int x2, int y2, int x3, int y3){
        return Math.abs(.5 * (x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)));
    }

    //TODO : NEEDS OPTIMIZATION
    public static void performCopy(String totalText, int selectionStart, int selectionEnd, @Nullable Context context){
        if (context != null) setClipboardManager(context);

        if (!Objects.equals(totalText, null)) {
            ClipData clip;
            try{
                copiedText = totalText.substring(selectionStart, selectionEnd);
                clip = ClipData.newPlainText(null,copiedText);
            }
            catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
                clip = ClipData.newPlainText(null,"");
            }
            clipboardManager.setPrimaryClip(clip);
//            Toast.makeText(context, "COPIED", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO : NEEDS OPTIMIZATION
    public static String performPaste(String totalText, int selectionStart, int selectionEnd, @Nullable Context context){
        if (context != null) setClipboardManager(context);

        String substring = totalText.substring(0, selectionStart);
        String substringL = totalText.substring(selectionEnd);
        String fString = substring.concat(""+ clipboardManager.getPrimaryClip().getItemAt(0).getText());

        Toast.makeText(context, "PASTED", Toast.LENGTH_SHORT).show();
        return fString.concat(substringL);
    }

    //TODO : NEEDS OPTIMIZATION
    public static String performCut(String totalText, int selectionStart, int selectionEnd, @Nullable Context context){
        if(context != null) setClipboardManager(context);

        if (!Objects.equals(totalText, null)) {
            ClipData clip;
            try{
                copiedText = totalText.substring(selectionStart, selectionEnd);
                clip = ClipData.newPlainText(null,copiedText);
            }
            catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
                clip = ClipData.newPlainText(null,"");
            }
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(context, "CUT", Toast.LENGTH_SHORT).show();
        }

        String substringS = totalText.substring(0,selectionStart);
        String substringE = totalText.substring(selectionEnd);

        return substringS.concat(substringE);
    }
}
