package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.mustonen.niko.hyperflightsimulatorultra3d.GameActivity;
import com.mustonen.niko.hyperflightsimulatorultra3d.R;

/**
 * Class for displaying prompt for asking username.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class CloudSaveUtil extends DialogFragment {

    /**
     * Generates name asking prompt.
     *
     * @param savedInstanceState Unused.
     * @return Prompt.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        builder.setView(inflater.inflate(R.layout.save_points, null))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) getDialog().findViewById(R.id.username);

                        if(editText != null) {
                            GameActivity ga = (GameActivity) getActivity();
                            ga.save(editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
