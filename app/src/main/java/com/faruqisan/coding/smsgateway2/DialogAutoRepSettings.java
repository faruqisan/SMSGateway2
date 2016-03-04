package com.faruqisan.coding.smsgateway2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Coding on 6/15/2015.
 */
public class DialogAutoRepSettings extends DialogFragment {

    String message;
    EditText autoRepSet;

    public DialogAutoRepSettings() {
        super();
    }

    public interface DialogAutoRepSettingsListener {
        public void onDialogPositiveClick(DialogFragment dialog,String message);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DialogAutoRepSettingsListener dialogAutoRepSettingsListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            dialogAutoRepSettingsListener=(DialogAutoRepSettingsListener)activity;
        }catch (Exception e){

        }
    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p/>
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     * <p/>
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        final View content=layoutInflater.inflate(R.layout.activity_autoreplysetting,null);

        builder.setView(content);
        builder.setTitle("Setting");

        builder.setPositiveButton(R.string.saveSetting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                autoRepSet=(EditText)content.findViewById(R.id.editTextAutoRepSetting);
                message=autoRepSet.getText().toString();
                dialogAutoRepSettingsListener.onDialogPositiveClick(DialogAutoRepSettings.this,message);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel
                dialog.cancel();
                dialogAutoRepSettingsListener.onDialogNegativeClick(DialogAutoRepSettings.this);

            }
        });


        return builder.create();
    }



}
