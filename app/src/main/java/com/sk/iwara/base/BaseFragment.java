package com.sk.iwara.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.sk.iwara.R;
import com.sk.iwara.util.LoadingUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型 BaseFragment：
 * 1. 自动反射创建 ViewBinding
 * 2. 统一状态栏、键盘、Toast、加载框、权限
 * 3. 不插手布局，子类只需实现 init()
 *
 * @param <VB> 对应的 ViewBinding 类型
 */
public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    protected VB binding;
    private Activity host;

    /* ======== 生命周期 ======== */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = requireActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        /* 1. 反射创建 Binding */
        binding = createBinding(inflater, container);
        /* 2. 公共 UI 初始化 */
        initCommonUI();
        /* 3. 业务入口 */
        init();
        new Thread(this::initData).start();
        host.runOnUiThread(this::initUI);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissLoading();
        binding = null;
    }

    /* ======== 反射生成 Binding ======== */
    @SuppressWarnings("unchecked")
    private VB createBinding(LayoutInflater inflater, ViewGroup parent) {
        try {
            Type superClass = getClass().getGenericSuperclass();
            ParameterizedType parameterized = (ParameterizedType) superClass;
            Class<VB> vbClass = (Class<VB>) parameterized.getActualTypeArguments()[0];
            Method inflateMethod = vbClass.getDeclaredMethod("inflate",
                    LayoutInflater.class, ViewGroup.class, boolean.class);
            return (VB) inflateMethod.invoke(null, inflater, parent, false);
        } catch (Exception e) {
            throw new RuntimeException("BaseFragment 反射创建 Binding 失败", e);
        }
    }

    /* ======== 子类必须实现 ======== */
    protected abstract void init();
    protected abstract void initData();
    protected abstract void initUI();

    /* ======== 公共 UI 初始化 ======== */
    protected void initCommonUI() {
        // 例：沉浸式状态栏
        setImmersiveStatusBar(true);
    }

    /* ======== 沉浸式状态栏 ======== */
    public void setImmersiveStatusBar(boolean light) {
        if (host instanceof BaseActivity) {
            ((BaseActivity<?>) host).setImmersiveStatusBar(light);
        }
    }


    /* ======== Toast ======== */
    public void toast(String msg) {
        Toast.makeText(host, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(String msg) {
        Toast.makeText(host, msg, Toast.LENGTH_LONG).show();
    }

    /* ======== 加载框（简易） ======== */
    private Dialog dialog;
    public void showLoading() {
        dialog= LoadingUtil.show(getContext(), R.mipmap.logo,false);
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /* ======== 权限申请 ======== */
    public interface PermissionCallback {
        void onGranted();
        void onDenied();
    }

    private PermissionCallback permissionCallback;

    public void requestPermission(String[] permissions, PermissionCallback callback) {
        this.permissionCallback = callback;
        requestPermissions(permissions, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissionCallback != null) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) permissionCallback.onGranted();
            else permissionCallback.onDenied();
        }
    }
}