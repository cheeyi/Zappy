package com.ongcheeyi.zappy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by CheeYi on 6/30/15.
 * Handles AlertDialog events
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Oops, failed!")
                .setMessage("Something went wrong!")
                .setPositiveButton("OK", null); // not doing anything upon press

    }
}
