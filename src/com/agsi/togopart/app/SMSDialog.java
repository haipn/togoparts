package com.agsi.togopart.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


@SuppressLint("ValidFragment")
public class SMSDialog extends DialogFragment {
	
	
	public SMSDialog(AlertPositiveListener listen) {
		super();
		alertPositiveListener = listen;
	}

	public static final String DATA = "items";

	public static final String SELECTED = "selected";
	/**
	 * Declaring the interface, to invoke a callback function in the
	 * implementing activity class
	 */
	AlertPositiveListener alertPositiveListener;

	/**
	 * An interface to be implemented in the hosting activity for "OK"
	 * button click listener
	 */
	public interface AlertPositiveListener {
		public void onPositiveClick(int position);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

		dialog.setTitle("Please Select Number:");
		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog alert = (AlertDialog)dialog;
			            int position = alert.getListView().getCheckedItemPosition();
			            alertPositiveListener.onPositiveClick(position);
					}
				});
		dialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		String[] list = bundle.getStringArray(DATA);
		int position = bundle.getInt(SELECTED);
		dialog.setSingleChoiceItems(list, position, null);
		return dialog.create();
	}
}
