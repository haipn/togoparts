package sg.togoparts.pro.login;

import sg.togoparts.pro.free.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
	boolean hasRm;

	public MyDialogFragment(OnSelectAction mCallback, boolean hasRemove) {
		super();
		this.mCallback = mCallback;
		this.hasRm = hasRemove;
	}

	public interface OnSelectAction {
		public void onCaptureSelect();

		public void onPickSelect();

		public void onRemoveSelect();
	}

	OnSelectAction mCallback;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.select_action).setItems(
				hasRm ? R.array.actions_extra : R.array.actions,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							mCallback.onCaptureSelect();
						} else if (which == 1)
							mCallback.onPickSelect();
						else {
							mCallback.onRemoveSelect();
						}
					}
				});
		return builder.create();
	}
}