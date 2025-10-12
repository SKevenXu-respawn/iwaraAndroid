package com.sk.iwara.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.sk.iwara.MainActivity;
import com.sk.iwara.R;
import com.sk.iwara.ui.Bbs.BbsActivity;
import com.sk.iwara.ui.Collect.CollectActivity;
import com.sk.iwara.ui.Login.LoginActivity;
import com.sk.iwara.ui.Settings.SettingActivity;
import com.sk.iwara.ui.Update.UpdateActivity;
import com.sk.iwara.ui.User.UserActivity;
import com.sk.iwara.ui.Video.VideoActivity;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.LoginSPUtil;

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
    private DrawerLayout drawerRoot;
    private NavigationView navView;
    private ViewGroup realContent;   // 真实内容容器
    /* ========== 生命周期 ========== */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 1. 创建 Binding */
        binding = createBinding();


        /* 0. 可选：抽屉+悬浮条注入（不启用时走原逻辑） */
        if (isUseDrawer() || isUseFloatBar()) {
            injectDrawerAndFloat();
            MaterialToolbar toolbar = findViewById(R.id.base_toolbar);
            toolbar.setNavigationOnClickListener(v -> openDrawer()); // ✅ 打开抽屉
        } else {
            // 原逻辑：反射 Binding
            binding = createBinding();
            setContentView(binding.getRoot());
        }


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
    private androidx.appcompat.app.AlertDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(new android.widget.ProgressBar(this))
                    .setCancelable(false)
                    .create();
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
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
        binding = null;
    }
    /* ========== 注入抽屉+悬浮条 ========== */
    private void injectDrawerAndFloat() {
        // 1. 统一根：DrawerLayout
        drawerRoot = new DrawerLayout(this);
        drawerRoot.setId(R.id.base_root);

        // 2. 主内容（带悬浮条）
        View contentWithFloat = LayoutInflater.from(this)
                .inflate(R.layout.base_content_with_float, drawerRoot, false);

        drawerRoot.addView(contentWithFloat);
        // 3. 把业务内容装进 real_content
        realContent = contentWithFloat.findViewById(R.id.real_content);
        realContent.addView(binding.getRoot()); // ✅ 把反射生成的 binding 装进容器
        // 3. 抽屉（仅启用时注入）
        if (isUseDrawer()) {
            navView = (NavigationView) LayoutInflater.from(this)
                    .inflate(R.layout.base_navigation_view, drawerRoot, false);
            drawerRoot.addView(navView, new DrawerLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics()),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    GravityCompat.START));

            navView.setNavigationItemSelectedListener(this::onNavItemSelected);
        }

        // 4. 悬浮条（仅启用时注入）
        if (isUseFloatBar()) {
            View floatBar = contentWithFloat.findViewById(R.id.float_root);
            onFloatBarCreated(floatBar); // 子类可覆写
        }

        if (LoginSPUtil.getInstance(this).get("access_token","null").equals("null")){
            navView.getHeaderView(0).findViewById(R.id.nav_header_login_data).setVisibility(View.GONE);
            navView.getHeaderView(0).findViewById(R.id.nav_header_login_thumb).setVisibility(View.GONE);
            navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setVisibility(View.VISIBLE);
            navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginSPUtil.getInstance(BaseActivity.this).clear();
                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                }
            });
        }else{
            navView.getHeaderView(0).findViewById(R.id.nav_header_login_data).setVisibility(View.VISIBLE);
            navView.getHeaderView(0).findViewById(R.id.nav_header_login_thumb).setVisibility(View.VISIBLE);
            navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setVisibility(View.GONE);
            TextView name= navView.getHeaderView(0).findViewById(R.id.nav_header_user_name);
            TextView username=navView.getHeaderView(0).findViewById(R.id.nav_header_user_username);
            ImageView imageView= navView.getHeaderView(0).findViewById(R.id.nav_header_user_image);
            TextView join= navView.getHeaderView(0).findViewById(R.id.nav_header_user_join);
            TextView lostLogin= navView.getHeaderView(0).findViewById(R.id.nav_header_user_last_login);
            TextView status= navView.getHeaderView(0).findViewById(R.id.nav_header_user_status);

            status.setText(LoginSPUtil.getInstance(this).get("status","null"));
            lostLogin.setText(DateUtil.FormatDate(LoginSPUtil.getInstance(this).get("lastLogin","null")));
            join.setText(DateUtil.FormatDate(LoginSPUtil.getInstance(this).get("join","null")));
            name.setText(LoginSPUtil.getInstance(this).get("username","null"));
            username.setText(LoginSPUtil.getInstance(this).get("name","null"));
            if (!LoginSPUtil.getInstance(this).get("thumb","null").equals("null")){
                GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/"+LoginSPUtil.getInstance(this).get("thumb","null"), new LazyHeaders.Builder()
                        .addHeader("User-Agent",
                                "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Mobile Safari/537.36 Edg/140.0.0.0")
                        .addHeader("Referer", "https://www.iwara.tv/")
                        .addHeader("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                        .addHeader("content-type", "image/jpeg")
                        // 如果浏览器带了 Cookie 也加进来
                        // .addHeader("Cookie", "session=xxx")
                        .build());
                Glide.with(imageView.getContext())
                        .load(glideUrl)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(imageView);
            }else{
                Glide.with(imageView.getContext())
                        .load(R.mipmap.no_icon)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(imageView);
            }
            navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BaseActivity.this, UserActivity.class));
                }
            });

        }
        setContentView(drawerRoot);
    }
    public void setItemSelect(int id){
        NavigationView navView=findViewById(R.id.nav_view);
        navView.setCheckedItem(id);
    }

    /* 子类可覆写：抽屉菜单点击 */

    public boolean onNavItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            Intent intent = null;
            navView.setCheckedItem(item);
            if (id==R.id.menu_update) {
                intent = new Intent(this, UpdateActivity.class);
            } else if (id==R.id.menu_talk) {
                intent = new Intent(this, BbsActivity.class);
            }
            else if (id==R.id.menu_setting) {
                intent = new Intent(this, SettingActivity.class);
            }else if (id==R.id.menu_collect) {
                intent = new Intent(this, CollectActivity.class);
            }else if (id==R.id.menu_video) {
                intent = new Intent(this, MainActivity.class);
            }

        // 确保每次点击都只启动一个实例
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        closeDrawer();
        startActivity(intent);

        // 关闭抽屉

        return true;
    }

    /* 子类可覆写：悬浮条创建完成 */
    protected void onFloatBarCreated(View floatBar) {}

    /* 工具：打开/关闭抽屉 */
    public void openDrawer() {
        if (drawerRoot != null) drawerRoot.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (drawerRoot != null) drawerRoot.closeDrawer(GravityCompat.START);
    }

    /* 工具：获取真实内容容器（无论是否注入抽屉/悬浮条） */
    public ViewGroup getRealContent() {
        if (drawerRoot != null) {
            // 启用抽屉/悬浮条时
            return findViewById(R.id.real_content);
        } else {
            // 原逻辑：直接返回 binding.getRoot()
            return (ViewGroup) binding.getRoot();
        }
    }

}