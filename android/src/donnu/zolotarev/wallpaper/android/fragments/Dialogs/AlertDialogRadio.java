package donnu.zolotarev.wallpaper.android.fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import donnu.zolotarev.wallpaper.android.R;
import donnu.zolotarev.wallpaper.android.adapters.ColorTextAdapter;
import donnu.zolotarev.wallpaper.android.adapters.ImageAdapter;

public class AlertDialogRadio extends DialogFragment {

    private static final String VALUE = "VALUE";
    private static final String NAMES = "NAMES";
    private static final String VALUES = "VALUES";
    private static final String TITLE = "TITLE";

    public static AlertDialogRadio get(int names, int values, String value, int title) {
        AlertDialogRadio radio = new AlertDialogRadio();
        Bundle b = new Bundle();
        b.putString(VALUE,value);
        b.putInt(NAMES, names);
        b.putInt(VALUES, values);
        b.putInt(TITLE, title);
        radio.setArguments(b);
        return radio;
    }

    public  interface AlertPositiveListener {
        public void onPositiveClick(String newVal);
    }

    private AlertPositiveListener alertPositiveListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int pos = 0;

        String val = bundle.getString(VALUE);
        String[] values = getResources().getStringArray(bundle.getInt(VALUES));
        for (int i = 0;i<values.length;i++){
            if (values[i].equals(val)) {
                pos = i;
                break;
            }
        }

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(bundle.getInt(TITLE));
//        b.setSingleChoiceItems(res, pos, null);
        ColorTextAdapter adapter = getAdapter();
        b.setSingleChoiceItems(adapter, pos, null);


        b.setPositiveButton("OK", null);
        AlertDialog d = b.create();
        d.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ColorTextAdapter) parent.getAdapter()).setSelectedIndex(position);
            }
        });
        adapter.setSelectedIndex(pos);
        return d;
    }

    private ColorTextAdapter getAdapter() {
        ColorTextAdapter adapte = new ColorTextAdapter(getActivity());
        int res = getArguments().getInt(NAMES);
        adapte.addAll(getResources().getStringArray(res));
        return adapte;
    }

    @Override
    public void onStart() {
        super.onStart();
        brandAlertDialog((AlertDialog) getDialog());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        AlertDialog alert = (AlertDialog)getDialog();
        int position = alert.getListView().getCheckedItemPosition();
        Bundle bundle = getArguments();
        String val = bundle.getString(VALUE);
        String[] values = getResources().getStringArray(bundle.getInt(VALUES));
        alertPositiveListener.onPositiveClick(values[position]);
        super.onDismiss(dialog);
    }

    public void setOnClickListener(AlertPositiveListener listener){
        alertPositiveListener = listener;
    }

    public static void brandAlertDialog(AlertDialog dialog) {
        try {
            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.text_color); // your color here


            View dec = dialog.getWindow().getDecorView();
            TextView alertTitle = (TextView) dec.findViewById(resources.getIdentifier("alertTitle", "id", "android"));
            alertTitle.setTextColor(color); // change title text color
            alertTitle.setTextSize(15);

            color = resources.getColor(R.color.background_setting_3);
            View titleDivider = dec.findViewById(resources.getIdentifier("titleDivider", "id", "android"));
            titleDivider.setBackgroundColor(resources.getColor(R.color.border)); // change divider color
            ViewGroup.LayoutParams params = titleDivider.getLayoutParams();
            params.height = 1;
            titleDivider.setLayoutParams(params);
            titleDivider.requestLayout();

            View topPanel = dec.findViewById(resources.getIdentifier("title_template", "id", "android"));
            topPanel.setBackgroundColor(color); // change divider color

            Button buttonPanel = (Button) dec.findViewById(resources.getIdentifier("button1", "id", "android"));
            buttonPanel.setBackgroundColor(color); // change divider color
            buttonPanel.setTextColor(resources.getColor(R.color.text_color));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
