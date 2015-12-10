package com.connectutb.xfuel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import java.util.HashMap;

/**
 * Created by eholst on 01.09.15.
 */
public class AdvancedOptionsDialog extends DialogFragment {

    private EditText ttl;
    private EditText oew;
    private EditText mtank;
    private Switch roundtrip;

    static AdvancedOptionsDialog newInstance(int num) {
        return new AdvancedOptionsDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.advanced_options_layout, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Send options
                        sendOptions();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdvancedOptionsDialog.this.getDialog().cancel();
                    }
                });

        ttl = (EditText) view.findViewById(R.id.editTextTTL);
        oew = (EditText) view.findViewById(R.id.editTextOEW);
        mtank = (EditText) view.findViewById(R.id.editTextMTANK);
        roundtrip = (Switch) view.findViewById(R.id.switchRoundTrip);

        return builder.create();
    }

    private void sendOptions(){

        HashMap options = new HashMap();
        String ttlVal = ttl.getText().toString();
        String oewVal = oew.getText().toString();
        String mtankVal = mtank.getText().toString();
        if (ttlVal.length()>0){
            options.put("TTL", ttlVal);
        }
        if (oewVal.length()>0){
            options.put("OEW", oewVal);
        }
        if (mtankVal.length()>0){
            options.put("MTANK", mtankVal);
        }

        if (roundtrip.isChecked()){
            options.put("TANKER", "AUTO");
        }

        Intent intent = new Intent("advancedOptions");
        intent.putExtra("options", options);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}