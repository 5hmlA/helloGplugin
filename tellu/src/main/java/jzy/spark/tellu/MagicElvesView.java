package jzy.spark.tellu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.lang.reflect.Constructor;
import java.util.Objects;
import jzy.spark.tellu.rule.Hogwarts;
import jzy.spark.tellu.rule.MagicAcademy;
import jzy.spark.tellu.rule.MagicCircle;
import jzy.spark.tellu.rule.MagicCircleImpl;
import org.jetbrains.annotations.NotNull;

public class MagicElvesView extends FrameLayout implements NestedScrollView.OnScrollChangeListener {

    private final LinearLayout.LayoutParams mDescLayoutParams;
    private final int mDividerColor;
    private final int mOrignAlpha;
    private final int dp24;
    private final View mLine;
    private TextView mDesc;
    private final TextView mTitle;
    private final LinearLayout.LayoutParams mTitleLayoutParams;
    private int dp50 = 1;
    private int dp40 = 1;
    private float dp07 = 1;
    private float dp48 = 1;
    private float dp16 = 1;
    private float dp31 = 1;
    private int mDescHeight;
    private final int mOrignColor;
    private String mOrignTips = "早上好哟早上好哟";
    private String mFixSmallTips = "健康";
    private int mMaxNeedScrollY = Integer.MAX_VALUE;
    MagicCircle magicCircle;


    public MagicElvesView(@NonNull Context context) {
        super(context);
        magicCircle = new ViewModelProvider(((FragmentActivity)context), new ViewModelProvider.Factory() {
            @NonNull
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
                try {
                    Constructor<T> constructor = modelClass.getConstructor(MagicAcademy.class);
                    return constructor.newInstance(new Hogwarts());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).get(MagicCircleImpl.class);
        inflate(context, R.layout.layout_spark_prompt, this);
        mTitle = findViewById(R.id.prompt_title);
//        mDesc = findViewById(R.id.prompt_desc);
        mLine = findViewById(R.id.prompt_line);
        mTitleLayoutParams = (LinearLayout.LayoutParams)mTitle.getLayoutParams();
        mDescLayoutParams = (LinearLayout.LayoutParams)mLine.getLayoutParams();
        dp50 = (int)dp2px(50);
        dp40 = (int)dp2px(40);
        dp24 = (int)dp2px(24);
        dp07 = dp2px(0.7F);
        dp48 = dp2px(48);
        dp31 = dp2px(31.333F);
        dp16 = dp2px(16.333F);
        //        mDividerColor = ContextCompat.getColor(parent.getContext(), R.color.common_line_devider);
        mDividerColor = Color.parseColor("#1A000000");
        mOrignColor = ((int)(Color.red(mDividerColor) * 255.0f + 0.5f) << 16) | ((int)(Color.green(mDividerColor) * 255.0f + 0.5f) << 8) | (int)(Color.blue(
                mDividerColor) * 255.0f + 0.5f);
        mOrignAlpha = Color.alpha(mDividerColor);

        magicCircle.obsEmotionChange().observe((LifecycleOwner)context, emotion -> {
            if (!TextUtils.isEmpty(emotion.emotion)) {
                mOrignTips = emotion.emotion;
                mDesc.setText(emotion.tips);
                mTitle.setText(mOrignTips);
            }
        });
    }

    public void attach(FrameLayout viewGroup, NestedScrollView nestedScrollView) {
        viewGroup.addView(this);
        nestedScrollView.setPadding(nestedScrollView.getPaddingStart(), dp2px(135), nestedScrollView.getPaddingRight(), nestedScrollView.getPaddingBottom());
        nestedScrollView.setClipToPadding(false);
        nestedScrollView.setOnScrollChangeListener(this);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > mMaxNeedScrollY) {
            return;
        }
        int scrolled = Math.min(dp50, dp2px(scrollY / 10F));
        float changing = (scrolled * 1F / dp50);
        //size: 40-16  margin 50-0  向上滚动 changing 0-1
        if (scrolled <= dp50) {
            mTitleLayoutParams.topMargin = dp50 - scrolled;
            if (mDescHeight == 0) {
                mDescHeight = mDesc.getMeasuredHeight();
            }
            if (changing > 0.5F) {
//                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--0> 1.0
//                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--1> -144.0
//                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--2> 144.0
                //ma 24 - 0
                int marg24 = (int)(dp48 - dp48 * changing);
                mDescLayoutParams.leftMargin = marg24;
                mDescLayoutParams.rightMargin = marg24;
                mDescLayoutParams.height = (int)dp07;
            } else {
//                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--0> 0.19333333
//                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--1> -94.0
//                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--2> 49.0
                //height - 0.7
                mDescLayoutParams.leftMargin = dp24;
                mDescLayoutParams.rightMargin = dp24;
                mDescLayoutParams.height = (int)(-dp31 * changing + dp16);
            }
            mDesc.setBackgroundColor(argb(changing * mOrignAlpha));
            if (changing >= 1) {
                mMaxNeedScrollY = scrollY;
                if (!Objects.equals(mTitle.getText(), mFixSmallTips)) {
                    mTitle.setText(mFixSmallTips);
                }
            } else {
                mMaxNeedScrollY = Integer.MAX_VALUE;
                if (!Objects.equals(mTitle.getText(), mOrignTips)) {
                    mTitle.setText(mOrignTips);
                }
            }
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp40 - 0.48F * scrolled);
        }
    }

    public int argb(float alpha) {
        return ((int) (alpha + 0.5f) << 24) | mOrignColor;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        magicCircle.spell();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        magicCircle.release();
    }
}
