package sg.togoparts.pro.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialog extends DialogFragment {

	public ErrorDialog(String mMessage) {
		super();
		this.mMessage = mMessage;
	}

	public ErrorDialog() {
		super();
	}

	public void setMessage(String msg) {
		mMessage = msg;
	}

	String mMessage;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage(mMessage)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		// .setNegativeButton(R.string.cancel, new
		// DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// // User cancelled the dialog
		// }
		// });
		// Create the AlertDialog object and return it
		Dialog ret = builder.create();
		ret.setCanceledOnTouchOutside(false);
		return ret;
	}
}
