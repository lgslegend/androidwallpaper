package donnu.zolotarev.wallpaper.android.fragments;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import donnu.zolotarev.wallpaper.android.AndroidLauncher;
import donnu.zolotarev.wallpaper.android.PhotoUtils;
import donnu.zolotarev.wallpaper.android.R;
import donnu.zolotarev.wallpaper.android.fragments.Dialogs.AlertDialogRadio;
import donnu.zolotarev.wallpaper.android.fragments.Dialogs.ImageDialogRadio;
import donnu.zolotarev.wallpaper.android.utils.Constants;
import donnu.zolotarev.wallpaper.android.utils.Utils;

import static donnu.zolotarev.wallpaper.android.utils.AndroidTypefaceUtility.FONT_ROBOTO_LIGHT;
import static donnu.zolotarev.wallpaper.android.utils.AndroidTypefaceUtility.FONT_ROBOTO_THIN;
import static donnu.zolotarev.wallpaper.android.utils.AndroidTypefaceUtility.setTypefaceOfView;

public class SettingFragment extends BaseFragment {

    @InjectView(R.id.main_set_wallpaper)
    Button setWallPaperBtn;

    @InjectView(R.id.setting_list_water)
    CheckBox waterRipple;

    @InjectView(R.id.setting_list_ripple_mode)
    CheckBox rippleInMove;

    View view1;
    View view2;
    View view3;
    View view4;

    private SharedPreferences setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setUpAlarm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = injectView(R.layout.fragment_setting,inflater, container);

        view1 = ButterKnife.findById(view, R.id.water_1);
        view2 = ButterKnife.findById(view, R.id.water_2);
        view3 = ButterKnife.findById(view, R.id.water_3);
        view4 = ButterKnife.findById(view, R.id.setting_list_rain_time);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        setTitle(ACTION_BAR_HIDE);
        setting = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //   loadFonts();
        updateCheckedUI();
        updateUI();
    }

    private void loadFonts() {
        try {
            setTypefaceOfView(getActivity(), getView(), FONT_ROBOTO_THIN);
            setTypefaceOfView(getActivity(), setWallPaperBtn, FONT_ROBOTO_LIGHT);
            setTypefaceOfView(getActivity(), ButterKnife.findById(getView(), R.id.setting_list_top_market_title), FONT_ROBOTO_LIGHT);
        } catch (Exception e) {
        }
    }

    private void updateCheckedUI() {
        waterRipple.setChecked(setting.getBoolean("ripple",true));
        rippleInMove.setChecked(setting.getBoolean("moveripple", false));
    }

    private void updateUI() {
        if (waterRipple.isChecked()) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        }else{
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.setting_list_water)
    void onClickWater(){
        setting.edit()
                .putBoolean("ripple", waterRipple.isChecked())
                .commit();
        updateUI();
    }

    @OnClick(R.id.setting_list_ripple_mode)
    void onClickRipple(){
        setting.edit()
                .putBoolean("moveripple", rippleInMove.isChecked())
                .commit();
    }

    @OnClick(R.id.main_set_wallpaper)
    void onSetWallpaper(){
        Intent i = new Intent();
        if (Build.VERSION.SDK_INT > 15) {
            i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);

            String p = AndroidLauncher.class.getPackage().getName();
            String c = AndroidLauncher.class.getCanonicalName();
            i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));
        } else {
            i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        startActivityForResult(i, 0);
    }

    @OnClick(R.id.setting_list_change_image_time)
    void onChangeTime(){

        AlertDialogRadio alert = AlertDialogRadio.get(R.array.change_image_time,R.array.change_image_time_value,setting.getString("time","30"),R.string.setting_list_change_image_time);
        alert.show(getFragmentManager(), "alert_dialog_radio");
        alert.setOnClickListener(new AlertDialogRadio.AlertPositiveListener() {
            @Override
            public void onPositiveClick(String newVal) {
                setting.edit()
                        .putString("time", newVal)
                        .commit();
            }
        });
    }

    @OnClick(R.id.setting_list_rain_time)
    void onRainTime(){
        AlertDialogRadio alert = AlertDialogRadio.get(R.array.rain_time,R.array.rain_time_value,setting.getString("rainTime",""), R.string.setting_list_rain_time);
        alert.show(getFragmentManager(), "alert_dialog_radio");
        alert.setOnClickListener(new AlertDialogRadio.AlertPositiveListener() {
            @Override
            public void onPositiveClick(String newVal) {
                setting.edit()
                        .putString("rainTime", newVal)
                        .commit();
            }
        });
    }

    @OnClick(R.id.setting_list_restore)
    void onRestore(){
        setting.edit().clear().commit();
        updateCheckedUI();
        setting.edit()
                .putBoolean("moveripple", rippleInMove.isChecked())
                .commit();
        updateUI();
    }


    @OnClick(R.id.setting_list_more_apps)
    void onMoreApps(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MORE_APPS)));
    }

    @InjectView(R.id.setting_list_set_custom_image_text)
    TextView imageTextView;

    @OnClick(R.id.setting_list_set_custom_image)
    void onSetCustomImage(){

        int pos = getImagePos();

        ImageDialogRadio alert = ImageDialogRadio.get(R.string.setting_list_set_custom_image,pos);
        alert.show(getFragmentManager(), "alert_dialog_radio");
        alert.setOnClickListener(new ImageDialogRadio.AlertPositiveListener() {
            @Override
            public void onPositiveClick(int newVal) {
                if (newVal == -1) {
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit()
                            .putString("customPhoto", "")
                            .commit();
                } else if (newVal != -2) {
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit()
                            .putString("customPhoto", "#" + newVal)
                            .commit();
                }
            }

            @Override
            public void onChoose() {
                PhotoUtils.importInGalery(SettingFragment.this, PhotoUtils.IN_GALLERY, PhotoUtils.TEMP_NAME);
            }
        });
    }

    private int getImagePos() {
        String s = setting.getString("customPhoto", "");
        if (s.isEmpty()) {
            return -1;
        }
        if (s.charAt(0) == '#') {
            s =  s.substring(1,s.length());
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PhotoUtils.IN_GALLERY) {
                PhotoUtils.onActivityResult(getActivity(), requestCode, resultCode, data);
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString("customPhoto", PhotoUtils.getLastPhotoPath())
                        .commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_share_app:
                Utils.share(getActivity(), getString(R.string.share_text));
                break;
        }

        return false;
    }
}
