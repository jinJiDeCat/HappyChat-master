package com.zz.zy.happychat.utils;

import android.content.Context;
import android.graphics.Bitmap;


public class BitmapUtils {
//    public static Bitmap blurBitmap(Context context,Bitmap bitmap){
//
//        //Let's create an empty bitmap with the same size of the bitmap we want to blur
//        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//        //Instantiate a new Renderscript
//        RenderScript rs = RenderScript.create(context.getApplicationContext());
//
//        //Create an Intrinsic Blur Script using the Renderscript
//        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//
//        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
//        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
//        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
//
//        //Set the radius of the blur: 0 < radius <= 25
//        blurScript.setRadius(25.0f);
//
//        //Perform the Renderscript
//        blurScript.setInput(allIn);
//        blurScript.forEach(allOut);
//
//        //Copy the final bitmap created by the out Allocation to the outBitmap
//        allOut.copyTo(outBitmap);
//
//        //recycle the original bitmap
//        bitmap.recycle();
//
//        //After finishing everything, we destroy the Renderscript.
//        rs.destroy();
//
//        return outBitmap;
//
//    }
}
