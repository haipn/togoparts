package sg.togoparts.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialog extends DialogFragment {
	public MessageDialog(String mMessage) {
		super();
		this.mMessage = mMessage;
	}
	public MessageDialog() {
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
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		Dialog ret = builder.create();
		ret.setCanceledOnTouchOutside(false);
		return ret;
	}
}
