package com.life.me.mutils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.life.me.R;

/**
 * Created by cuiyang on 15/12/22.
 */
public class Widget_Utils {
    private static AlertDialog.Builder dialog;

    public static void showDialog(Context mContext, String Content) {
        dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(Content);
        dialog.setNegativeButton("确定", null);
        dialog.show();
    }

    public static void showSnackbar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundResource(R.color.background_allShareFragment3);
        snackbar.show();
    }

    public static Snackbar setSnackbarClicklistener(View view, String msg, String actionTxt, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundResource(R.color.background_allShareFragment3);
        snackbar.setAction(actionTxt, listener);
//        View text = snackbar.getView();
//        ((TextView) text.findViewById(R.id.snackbar_action)).setTextColor(Color.parseColor("#FFFFFF"));
        return snackbar;
    }
}
