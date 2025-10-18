package com.sk.iwara.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;

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
    /* ========== 生命周期 ========== */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 1. 创建 Binding */
        binding = createBinding();



            binding = createBinding();
            setContentView(binding.getRoot());


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

}