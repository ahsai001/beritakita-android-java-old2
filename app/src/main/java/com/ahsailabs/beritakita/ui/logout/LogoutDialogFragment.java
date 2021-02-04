package com.ahsailabs.beritakita.ui.logout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ahsailabs.beritakita.utils.InfoUtil;
import com.ahsailabs.beritakita.utils.SessionUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Created by ahmad s on 09/09/20.
 */
public class LogoutDialogFragment extends DialogFragment {
    public LogoutDialogFragment(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure?")
                .setPositiveButton("logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionUtil.logout(getActivity());
                        InfoUtil.showToast(getActivity(), "logout anda berhasil");
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return dialogBuilder.create();
    }
}
