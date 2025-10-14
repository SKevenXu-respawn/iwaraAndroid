package com.sk.iwara.ui.Settings;

import android.util.Log;
import android.widget.CompoundButton;

import com.sk.iwara.R;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.ActivitySettingBinding;
import com.sk.iwara.util.SPUtil;

public class SettingFragment extends BaseFragment<ActivitySettingBinding> {

    @Override
    protected void init() {
        binding.settingOfficeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.putBoolean("office",b);
            }
        });
        binding.settingBossBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.putBoolean("boss",b);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {
        binding.settingOfficeBtn.setChecked( SPUtil.getBoolean("office",false));
        binding.settingBossBtn.setChecked( SPUtil.getBoolean("boss",false));
        Log.d("temp","tenmpo");
    }
}
