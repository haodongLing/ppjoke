package com.haodong.practice.ppjoke.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haodong.practice.libcommon.utils.PixUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * created by linghaoDo on 2020/9/10
 * description:
 * <p>
 * version:
 */
public class PPImageView extends AppCompatImageView {
    public PPImageView(@NonNull Context context) {
        this(context, null);
    }

    public PPImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PPImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String imageUrl) {
        setImageUrl(this, imageUrl, false);
    }

    @BindingAdapter(value = {"image_url", "isCircle"})
    public static void setImageUrl(PPImageView view, String imageUrl, boolean isCircle) {
        view.setImageUrl(view, imageUrl, isCircle, 0);
    }


    @BindingAdapter(value = {"image_url", "isCircle", "radius"}, requireAll = false)
    public static void setImageUrl(PPImageView view, String imageUrl, boolean isCircle, int radius) {
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {
            builder.transform(new CircleCrop());
        } else if (radius > 0) {
            builder.transform(new RoundedCornersTransformation(PixUtils.dp2px(radius), 0));
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);
    }

    public void bindData(int widthPx, int heightPx, int marginLeft, String imageUrl) {
        bindData(widthPx, heightPx, marginLeft, PixUtils.getScreenWidth(), PixUtils.getScreenHeight(), imageUrl);
    }

    public void bindData(int widthPx, int heightPx, final int marginLeft, final int maxWidth, final int maxHeight, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        if (widthPx <= 0 || heightPx <= 0) {
            Glide.with(this).load(imgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);

                    setImageDrawable(resource);
                }
            });
        }
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f) / finalWidth);
        } else {
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));
        }
        ViewGroup.LayoutParams params=getLayoutParams();
        params.width=finalWidth;
        params.height=finalHeight;
        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        } else if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        }
        setLayoutParams(params);
    }

    @BindingAdapter(value = {"blur_url", "radius"})
    public static void setBlurImageUrl(ImageView imageView, String blurUrl, int radius) {
        Glide.with(imageView).load(blurUrl).override(radius)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setBackground(resource);
                    }
                });
    }

}
