package com.life.me.mutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.squareup.picasso.Transformation;

/**
 * Created by cuiyang on 15/12/21.
 */
public class BulerTransformation implements Transformation {
    private Context mContext;

    public BulerTransformation(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        return blur(source, mContext);
    }

    @Override
    public String key() {
        return "square()";
    }

    /**
     * 制作蒙板
     */
    public Bitmap blur(Bitmap bkg, Context mContext) {
        try {
            RenderScript rs = RenderScript.create(mContext);
            Allocation overlayAlloc = Allocation.createFromBitmap(rs, bkg);
            ScriptIntrinsicBlur blur =
                    ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
            blur.setInput(overlayAlloc);
            blur.setRadius(15);
            blur.forEach(overlayAlloc);
            overlayAlloc.copyTo(bkg);
            return bkg;
        } catch (Exception e) {
            Log.e(getClass().getName(), "blur_error==" + e.getMessage());
        }
        return null;
    }

}
