package sg.togoparts.pro.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorInternetDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setTitle("Connection error")
				.setMessage("Unable to connect with the server. Check your internet connection and try again")
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getActivity().onBackPressed();
							}
						}).create();
	}
}
