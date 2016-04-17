package com.github.skarllot.android.skllib;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides methods to define UI behaviours for specific actions.
 */
public final class UIBehaviour {
    /**
     * Defines the minimum diagonal inches expected for tablets.
     */
    public static final float TABLET_MINIMUM_DIAGONAL_INCHES = 7f;

    /**
     * Gets the activity associated with specified view.
     *
     * @param view A view.
     * @return The requested activity if found.
     */
    @Nullable
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity)
                return (Activity) context;

            context = ((ContextWrapper) context).getBaseContext();
        }

        return null;
    }

    /**
     * Gets the diagonal screen size as measured in inches.
     *
     * @param context Current context.
     * @return Diagonal inches.
     */
    public static float getDiagonalInches(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        float xInches = metrics.widthPixels / metrics.xdpi;
        float yInches = metrics.heightPixels / metrics.ydpi;
        double diagonal = Math.sqrt(xInches * xInches + yInches * yInches);

        return (float) diagonal;
    }

    /**
     * Gets the screen height as measured in density pixels.
     *
     * @param context Current context.
     * @return Height in density pixels.
     */
    public static float getHeightDp(Context context) {
        return getHeightDp(context.getResources().getDisplayMetrics());
    }

    /**
     * Gets the screen height as measured in density pixels.
     *
     * @param metrics Display metrics.
     * @return Height in density pixels.
     */
    public static float getHeightDp(DisplayMetrics metrics) {
        return metrics.heightPixels / metrics.density;
    }

    /**
     * Gets the screen width as measured in density pixels.
     *
     * @param context Current context.
     * @return Width in density pixels.
     */
    public static float getWidthDp(Context context) {
        return getWidthDp(context.getResources().getDisplayMetrics());
    }

    /**
     * Gets the screen width as measured in density pixels.
     *
     * @param metrics Display metrics.
     * @return Width in density pixels.
     */
    public static float getWidthDp(DisplayMetrics metrics) {
        return metrics.widthPixels / metrics.density;
    }

    /**
     * Shows an alert message to user.
     *
     * @param context The current context.
     * @param msg     The message to output.
     */
    public static void showAlertMessage(Context context, String msg) {
        //Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows an alert message to user.
     *
     * @param view     The View which the message is linked to.
     * @param msg      The message to output.
     * @param setFocus Defines whether should request focus to View.
     */
    public static void showAlertMessage(View view, String msg, boolean setFocus) {
        showAlertMessage(view.getContext(), msg);

        if (setFocus) {
            view.requestFocus();
        }
    }

    /**
     * Hides the soft keyboard from screen.
     *
     * @param activity Current activity.
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
