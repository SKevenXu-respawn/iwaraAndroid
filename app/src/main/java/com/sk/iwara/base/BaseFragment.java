package com.sk.iwara.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sk.iwara.R;
import com.sk.iwara.util.LoadingUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

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
    private BottomSheetDialog sheet ;

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
        host.runOnUiThread(()-> {
            try {
                sheet = new BottomSheetDialog(getContext(),R.style.TransparentBottomSheet);
                initUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
    protected abstract void initUI() throws IOException;

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
        if (getActivity()!=null){
            requireActivity().runOnUiThread(()->{
                dialog= LoadingUtil.show(requireContext(), R.mipmap.logo,false);
            });

        }

    }

    public void dismissLoading() {
        if (getActivity()!=null){
            getActivity().runOnUiThread(()->{
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });
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
    protected void showTextLog(String text) {


        sheet.getWindow().setDimAmount(0f);          // 关键
        sheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 必须在 setContentView 之前调用
        sheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View root = LayoutInflater.from(getContext())
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
        BottomSheetDialog sheet = new BottomSheetDialog(getContext());
        /* 透明背景（你已有） */
        sheet.getWindow().setDimAmount(0f);
        sheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View root = LayoutInflater.from(getContext()).inflate(R.layout.catch_layout, null, false);
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