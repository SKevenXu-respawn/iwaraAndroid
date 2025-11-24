package com.sk.iwara.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.sk.iwara.R;

import com.sk.iwara.util.LoadingUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型 BaseActivity：
 * 1. 自动反射创建 ViewBinding
 * 2. 提供沉浸式、软键盘、权限、加载框、Toast 等常用工具
 * 3. 子类仅需实现 init()
 *
 * @param <VB> 对应的 ViewBinding 类型
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB binding;
    private Dialog dialog;
    private DrawerLayout drawerRoot;
    private NavigationView navView;
    private ViewGroup realContent;   // 真实内容容器
    private BottomSheetDialog sheet ;
    /* ========== 生命周期 ========== */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 1. 创建 Binding */
        binding = createBinding();



            binding = createBinding();
            setContentView(binding.getRoot());

        sheet = new BottomSheetDialog(this,R.style.TransparentBottomSheet);
        /* 2. 初始化通用 UI */
        initCommonUI();

        /* 3. 业务初始化 */
        init();
        initUI();
        initData();
    }
    /* ========== 可选开关 ========== */
    protected boolean isUseDrawer() { return true; }   // 子类复写 true 即可启用
    protected boolean isUseFloatBar() { return true; } // 子类复写 true 即可启用


    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(()-> updateUI());

        updateData();
    }

    /* 反射生成 Binding：MyActivity -> ActivityMainBinding.inflate(getLayoutInflater()) */
    @SuppressWarnings("unchecked")
    private VB createBinding() {
        try {
            /* 拿到泛型实参 <VB> */
            Type superClass = getClass().getGenericSuperclass();
            ParameterizedType parameterized = (ParameterizedType) superClass;
            Class<VB> vbClass = (Class<VB>) parameterized.getActualTypeArguments()[0];

            /* 取静态方法 inflate(LayoutInflater) */
            Method inflateMethod = vbClass.getDeclaredMethod("inflate", android.view.LayoutInflater.class);
            return (VB) inflateMethod.invoke(null, getLayoutInflater());
        } catch (Exception e) {
            throw new RuntimeException("BaseActivity 反射创建 Binding 失败", e);
        }
    }

    /* 子类必须实现：写业务逻辑 */
    protected abstract void init();
    protected abstract void initData();
    protected abstract void initUI();
    protected  void updateUI(){};
    protected  void updateData(){};
    public void onConfigTitltBar(){

    }
    /* ========== 通用 UI 初始化 ========== */
    protected void initCommonUI() {
        // 例：沉浸栏
        setImmersiveStatusBar(true);
    }

    /* ========== 沉浸栏 ========== */
    protected void setImmersiveStatusBar(boolean lightStatusBar) {
        View decor = getWindow().getDecorView();
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (lightStatusBar) {
            flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decor.setSystemUiVisibility(flag);
    }

    /* ========== 软键盘 ========== */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /* ========== Toast ========== */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void toastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /* ========== 加载框（简易） ========== */

    public void showLoading() {
        dialog= LoadingUtil.show(this,R.mipmap.logo,false);
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /* ========== 权限申请封装（可选） ========== */
    public interface PermissionCallback {
        void onGranted();
        void onDenied();
    }

    private PermissionCallback permissionCallback;

    public void requestPermission(String[] permissions, PermissionCallback callback) {
        this.permissionCallback = callback;
        androidx.core.app.ActivityCompat.requestPermissions(this, permissions, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted && permissionCallback != null) {
                permissionCallback.onGranted();
            } else if (permissionCallback != null) {
                permissionCallback.onDenied();
            }
        }
    }

    /* ========== 内存泄漏保护 ========== */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
    /* ========== 注入抽屉+悬浮条 ========== */
    protected void showTextLog(String text) {


        sheet.getWindow().setDimAmount(0f);          // 关键
        sheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 必须在 setContentView 之前调用
        sheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View root = LayoutInflater.from(this)
                .inflate(R.layout.catch_layout, null, false);
        root.setBackgroundColor(Color.TRANSPARENT);
        sheet.setContentView(root);

        TextView tvContent = root.findViewById(R.id.catch_text);
        TextView btnToggle = root.findViewById(R.id.catch_detail_text);
        TextView closeBtn=root.findViewById(R.id.catch_close);
        tvContent.setText(text);
        // 展开/收起逻辑
        btnToggle.setOnClickListener(v -> {
            boolean isExpanded = tvContent.getMaxLines() == Integer.MAX_VALUE;
            if (isExpanded) {
                tvContent.setMaxLines(2);
                btnToggle.setText("展开");
            } else {
                tvContent.setMaxLines(Integer.MAX_VALUE);
                btnToggle.setText("收起");
            }
        });
        closeBtn.setOnClickListener(v->{
            sheet.dismiss();
        });

        sheet.show();
    }


    protected void showTextLogAutoTime(String text,long time) {
        BottomSheetDialog sheet = new BottomSheetDialog(this);

        /* 透明背景（你已有） */
        sheet.getWindow().setDimAmount(0f);
        sheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View root = LayoutInflater.from(this).inflate(R.layout.catch_layout, null, false);
        sheet.setContentView(root);

        TextView tvContent = root.findViewById(R.id.catch_text);
        TextView btnToggle = root.findViewById(R.id.catch_detail_text);
        TextView closeBtn = root.findViewById(R.id.catch_close);

        tvContent.setText(text);

        /* 展开/收起（你已有） */
        btnToggle.setOnClickListener(v -> {
            boolean isExpanded = tvContent.getMaxLines() == Integer.MAX_VALUE;
            tvContent.setMaxLines(isExpanded ? 2 : Integer.MAX_VALUE);
            btnToggle.setText(isExpanded ? "展开" : "收起");
        });

        closeBtn.setOnClickListener(v -> sheet.dismiss());

        sheet.show();
        if (time<0||time==0){
            /* 倒计时自动关闭：这里设为 5 秒（5000 ms） */
            root.postDelayed(() -> {
                if (sheet.isShowing()) sheet.dismiss();
            },5000);   // ← 改这里即可调整时长
        }else{
            /* 倒计时自动关闭：这里设为 5 秒（5000 ms） */
            root.postDelayed(() -> {
                if (sheet.isShowing()) sheet.dismiss();
            },time);   // ← 改这里即可调整时长
        }

    }
    protected void dismissTextLog(){
        if (sheet!=null){
            sheet.dismiss();
        }

    }
}