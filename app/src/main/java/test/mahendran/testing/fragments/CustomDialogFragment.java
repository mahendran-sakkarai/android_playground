package test.mahendran.testing.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import test.mahendran.testing.R;

/**
 * Created by mahendran on 29/12/15.
 */
public class CustomDialogFragment extends DialogFragment {
    public CustomDialogFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){
        Dialog dialog = super.onCreateDialog(onSavedInstanceState);

        // Hide default title.
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View v = inflater.inflate(R.layout.custom_dialog_layout, container, false);

        ImageView backButton = (ImageView) v.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO handle the Back button
            }
        });

        Button okButton = (Button) v.findViewById(R.id.ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO handle the ok Button
            }
        });

        Button cancelButton = (Button) v.findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO handle the cancel Button
            }
        });

        return v;
    }
}
