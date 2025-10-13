package com.sk.iwara.util;

/**
 * Created by 25140 on 2025/10/13 .
 */
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.animation.*;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class LoadingUtil {

    private  final Dialog dialog;
    private  final ImageView imageView;

    /**
     * 显示旋转动画弹窗
     *
     * @param context    上下文
     * @param imgRes     图片资源（任意 drawable）
     * @param cancelable 是否允许返回键取消
     * @return 弹窗实例，可继续控制（关闭/再次显示）
     */
    public static Dialog show(@NonNull Context context,
                              @DrawableRes int imgRes,
                              boolean cancelable) {
        return new LoadingUtil(context, imgRes, cancelable).dialog;
    }

    /* 私有构造 */
    private  LoadingUtil(Context ctx, int imgRes, boolean cancelable) {
        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);          // 关键：是否可取消
        dialog.setCanceledOnTouchOutside(cancelable);   // 点外部不消失
        imageView = new ImageView(ctx);
        imageView.setImageResource(imgRes);
        imageView.setPadding(20, 20, 20, 20);

        // 开始无限循环动画
        startRotateAnim(imageView);

        dialog.setContentView(imageView);

        // 透明背景 + 居中
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(200,
                    200);
            window.setGravity(Gravity.CENTER);
        }

        dialog.show();
    }

    /* 3 段旋转 + 回弹 + 无限循环 */
    private  void startRotateAnim(ImageView iv) {
        iv.setRotation(0);
        iv.animate()
                .rotationBy(180)                 // 每次调用 +270°
                .setDuration(1000)                // 与 XML 三段总时长一致
                .setInterpolator(new OvershootInterpolator(6f))
                .withEndAction(() -> startRotateAnim(iv)) // 循环
                .start();
    }

    /* 外部主动关闭 */
    public  void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}