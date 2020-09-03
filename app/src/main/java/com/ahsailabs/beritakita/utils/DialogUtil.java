package com.ahsailabs.beritakita.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

/**
 * Created by ahmad s on 22/08/20.
 */
class DialogUtil {
    public static androidx.appcompat.app.AlertDialog showDialog2Option(Context context, String title, String msg, String strOption1, final Runnable option1, String strOption2, final Runnable option2) {
        return showDialog3OptionWithIcon(context, null, title,msg,strOption1,option1,strOption2,option2,null,null);
    }

    public static androidx.appcompat.app.AlertDialog showDialog3OptionWithIcon(Context context, Drawable icon, String title, String msg, String strOption1, final Runnable option1, String strOption2, final Runnable option2, String strOption3, final Runnable option3) {
        return showDialog3OptionWithIcon(context,icon,title,msg,strOption1,option1,true,strOption2,option2,true,strOption3,option3,true);
    }

    public static Spanned fromHtml(String html){
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static androidx.appcompat.app.AlertDialog showDialog3OptionWithIcon(Context context, Drawable icon, String title, String msg, String strOption1, final Runnable option1, final boolean dismissByOption1, String strOption2, final Runnable option2, final boolean dismissByOption2, String strOption3, final Runnable option3, final boolean dismissByOption3) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage(fromHtml(msg)).setCancelable(false);
        if(icon != null) {
            builder.setIcon(icon);
        }

        if(strOption2 != null) {
            builder.setNeutralButton(strOption2, null);
        }

        if(strOption1 != null) {
            builder.setPositiveButton(strOption1, null);
        }

        if(strOption3 != null) {
            builder.setNegativeButton(strOption3, null);
        }

        final androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();

        //set custom button
        if(strOption2 != null) {
            alert.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (option2 != null) {
                        option2.run();
                    }
                    if(dismissByOption2) alert.dismiss();
                }
            });
        }

        if(strOption1 != null) {
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (option1 != null) {
                        option1.run();
                    }
                    if(dismissByOption1) alert.dismiss();
                }
            });
        }

        if(strOption3 != null) {
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (option3 != null) {
                        option3.run();
                    }
                    if(dismissByOption3) alert.dismiss();
                }
            });

        }

        return alert;
    }
}
