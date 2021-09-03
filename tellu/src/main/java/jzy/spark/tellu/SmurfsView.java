package jzy.spark.tellu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import java.util.concurrent.TimeUnit;
import jzy.spark.tellu.data.Emotion;
import jzy.spark.tellu.rule.Hogwarts;
import jzy.spark.tellu.rule.MagicAcademy;
import jzy.spark.tellu.rule.MagicCircle;
import jzy.spark.tellu.rule.MagicCircleImpl;
import org.jetbrains.annotations.NotNull;

public class SmurfsView extends FrameLayout implements NestedScrollView.OnScrollChangeListener {

    protected final LinearLayout.LayoutParams mBottomLineParams;
    protected final int mDividerColor;
    protected final float mOrignAlpha;
    protected final int dp24;
    protected final View mLine;
    protected final int dp12;
    NestedScrollView mNestedScrollView;
    protected TextView mDesc;
    protected final TextView mTitle;
    protected final LinearLayout.LayoutParams mTitleLayoutParams;
    protected int dp50 = 1;
    protected int dp40 = 1;
    protected float dp48 = 1;
    protected final int mOrignColor;
    protected String mOrignTips = " ";
    protected String mFixSmallTips = "健康";
    protected int mMaxNeedScrollY = Integer.MAX_VALUE;
    protected float mChanging = 0;//默认是展开的
    protected final LinearLayout.LayoutParams mTipsLineParam;
    protected int mTipsHeight;
    protected final int SCROLLSPRING = 3;
    protected final Handler mHandler = new Handler();
    protected boolean mFingerUp;
    protected Emotion mNextEmotion;
    protected long titleFoldStateChangeTimeStemp;
    protected final Runnable mR = new Runnable() {
        @Override
        public void run() {
            int totalScrolly = dp50 * SCROLLSPRING;
            int scrollY = mNestedScrollView.getScrollY();
            if (scrollY > 0 && scrollY < totalScrolly) {
                if (scrollY > totalScrolly / 2) {
                    mNestedScrollView.smoothScrollTo(0, totalScrolly);
                } else {
                    unFold();
                }
            }
        }
    };
    protected int mOrignScrollViewPadingTop;
    protected int mPaddingToAdd;
    protected boolean needUpdateWhenUnFold;

    MagicCircle mCircle;

    public SmurfsView(@NonNull Context context) {
        super(context);
        mCircle = new ViewModelProvider(((FragmentActivity)context), new ViewModelProvider.Factory() {
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
        setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.prompt_title);
        mDesc = findViewById(R.id.prompt_desc);
        mLine = findViewById(R.id.prompt_line);
        setOnClickListener(v -> {
            if (mChanging != 0) {
                mNestedScrollView.smoothScrollTo(0, 0);
            }
        });
        mTitleLayoutParams = (LinearLayout.LayoutParams)mTitle.getLayoutParams();
        mBottomLineParams = (LinearLayout.LayoutParams)mLine.getLayoutParams();
        mTipsLineParam = (LinearLayout.LayoutParams)mDesc.getLayoutParams();
        dp50 = (int)dp2px(50);
        dp40 = (int)dp2px(40);
        dp24 = (int)dp2px(24);
        dp48 = dp2px(48);
        dp12 = dp2px(12);
//        mDividerColor = ContextCompat.getColor(getContext(), R.color.common_line_devider);
        mDividerColor = Color.parseColor("#1A000000");
        int alpha = Color.alpha(mDividerColor);
        if (alpha < 255) {
            mOrignColor = Color.argb(255, Color.red(mDividerColor),Color.green(mDividerColor),Color.blue(mDividerColor));
        } else {
            mOrignColor = mDividerColor;
        }
        Utills.Companion.elog("line devider ",alpha, mOrignColor, mDividerColor);
        mOrignAlpha = alpha / 255F;
        mLine.setBackgroundColor(mOrignColor);
        mLine.setAlpha(0);

        mCircle.obsEmotionChange().observe((LifecycleOwner)context, this::updateEmotion);
        post(this::unFold);
    }

    public void updateEmotion(Emotion emotion) {
        if (!isUnFold()) {
            //没展开 不更新
            mNextEmotion = emotion;
            return;
        }
        mNextEmotion = null;
        if (!TextUtils.isEmpty(emotion.emotion)) {
            mOrignTips = emotion.emotion;
            mDesc.setText(emotion.tips);
            mTitle.setText(mOrignTips);
            mTipsHeight = mDesc.getMeasuredHeight();
        }

        getTipsHeightAndSetPading();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void attach(NestedScrollView nestedScrollView) {
        Utills.Companion.elog("PrompLayout attach ", nestedScrollView, this);
        //<editor-fold desc="包裹一层">
        FrameLayout parent = (FrameLayout)nestedScrollView.getParent();
        parent.removeAllViews();
        FrameLayout frameWrapper = new FrameLayout(nestedScrollView.getContext());
        frameWrapper.setPadding(0,dp2px(10),0,0);
        frameWrapper.addView(nestedScrollView, -1, -2);
        frameWrapper.addView(this, -1, -2);
        parent.addView(frameWrapper, -1, -1);
        //</editor-fold>

        mNestedScrollView = nestedScrollView;
        nestedScrollView.setClipToPadding(false);
        nestedScrollView.setOnScrollChangeListener(this);
        nestedScrollView.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                mFingerUp = true;
                mHandler.postDelayed(mR, 50);
            } else {
                mFingerUp = false;
            }
            return false;
        });
        View titleLayout = findViewById(R.id.layout_text_ll);
        titleLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        scrollViewPadingTop(titleLayout.getMeasuredHeight());
        getTipsHeightAndSetPading();
        onScrollChange(mNestedScrollView, 0, 0, 0, 0);
    }

    private void getTipsHeightAndSetPading() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View titleLayout = findViewById(R.id.layout_text_ll);
                int measuredHeight = titleLayout.getMeasuredHeight();
                mOrignScrollViewPadingTop = measuredHeight;
                mPaddingToAdd = dp50 * 4 - mOrignScrollViewPadingTop;
                scrollViewPadingTop(measuredHeight);
                mTipsHeight = mDesc.getMeasuredHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void scrollViewPadingTop(int measuredHeight) {
        if (measuredHeight == 0) {
            return;
        }
        mNestedScrollView.setPadding(mNestedScrollView.getPaddingStart(), measuredHeight, mNestedScrollView.getPaddingRight(), mNestedScrollView.getPaddingBottom());
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mHandler.removeCallbacks(mR);
        if (scrollY > mMaxNeedScrollY) {
            return;
        }
        if (mFingerUp) {
            mHandler.postDelayed(mR, 50);
        }
        int scrolled = Math.min(dp50, scrollY / SCROLLSPRING);
        float changing = scrolled * 1F / dp50;
        if (mChanging == changing) {
            return;
        }
        mChanging = changing;
        scrollViewPadingTop((int)(mOrignScrollViewPadingTop + mPaddingToAdd * changing));
        //size: 40-16  margin 50-0  向上滚动 changing 0-1
        if (scrolled <= dp50) {
            mTitleLayoutParams.topMargin = dp50 - scrolled;
            if (mChanging > 0.5F) {
                //                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--0> 1.0
                //                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--1> -144.0
                //                2021-02-08 10:37:59.721 5093-5093/com.op.myapplication I/System.out: calculate--2> 144.0
                //ma 24 - 0
                int marg24 = (int)(dp48 - dp48 * mChanging);
                mBottomLineParams.leftMargin = marg24;
                mBottomLineParams.rightMargin = marg24;
                mTipsLineParam.topMargin = (int)((mTipsHeight) * -1);
                mDesc.setAlpha(0);
                mBottomLineParams.topMargin = 0;
            } else {
                //                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--0> 0.19333333
                //                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--1> -94.0
                //                2021-02-08 10:37:04.446 5093-5093/com.op.myapplication I/System.out: calculate--2> 49.0
                //height - 0.7
                mBottomLineParams.leftMargin = dp24;
                mBottomLineParams.rightMargin = dp24;
                mBottomLineParams.topMargin = (int)(dp12 * (1 - mChanging * 2));
                mTipsLineParam.topMargin = (int)((mTipsHeight) * mChanging * -2);
                //建议：加快情感化文本消失的时长，尽量避免文本叠加
                if (mChanging > 1 / 3F) {
                    mDesc.setAlpha(0);
                }else {
                    mDesc.setAlpha(1 - mChanging * 3);
                }
            }
            mLine.setAlpha(mChanging * mOrignAlpha);
            if (mChanging >= 1) {
                mMaxNeedScrollY = scrollY;
                if (!Objects.equals(mTitle.getText(), mFixSmallTips)) {
                    mTitle.setText(mFixSmallTips);
                }
                if (mTitle.getTag() == null) {
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    mTitle.setTag("bold");
                }
            } else {
                mMaxNeedScrollY = Integer.MAX_VALUE;
                if (!Objects.equals(mTitle.getText(), mOrignTips)) {
                    mTitle.setText(mOrignTips);
                }
                if (mTitle.getTag() != null) {
                    mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    mTitle.setTag(null);
                }
            }

            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp40 - 0.48F * scrolled);
            if (isUnFold()) {
                titleFoldStateChangeTimeStemp = System.currentTimeMillis();
                if ((mNextEmotion != null)) {
                    updateEmotion(mNextEmotion);
                }
                if (needUpdateWhenUnFold) {
                    needUpdateWhenUnFold = false;
                    Utills.Companion.elog("update emotion when unfold");
                    mCircle.spell();
                }
                //                Utills.Companion.elog("SmurfsView -->>  标题展开了", mTipsHeight);
            } else if (isFold()) {
                titleFoldStateChangeTimeStemp = System.currentTimeMillis();
                //                Utills.Companion.elog("SmurfsView -->>  标题折叠了", mTipsHeight);
            }
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int dp2px(float dpValue) {
        return (int)(0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isNeedCheckUpdate()) {
            //没展开的时候 不更新
            return;
        }
        mCircle.spell();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!isNeedCheckUpdate()) {
            //没展开的时候 不更新
            Utills.Companion.elog("set flag to update emotion when unfold");
            needUpdateWhenUnFold = true;
            return;
        }
        if (hasWindowFocus) {
            mCircle.spell();
        }
    }

    /**
     * 是否需要更新 标题内容
     * 标题展开的时候 需要检测更新
     * 标题一直折叠 超过半小时 需要更新
     *
     * @return
     */
    private boolean isNeedCheckUpdate() {
        if (isUnFold()) {
            return true;
        }
        if ((isFold() && TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - titleFoldStateChangeTimeStemp) > 30)) {
            needUpdateWhenUnFold = true;
            unFold();//自动展开
            return true;
        }
        return false;
    }

    /**
     * 标题 是否 展开 了
     *
     * @return
     */
    private boolean isUnFold() {
        return mChanging == 0;
    }

    /**
     * 标题 是否 折叠起来 了
     *
     * @return
     */
    private boolean isFold() {
        return mChanging == 1;
    }

    /**
     * 展开 标题
     */
    private void unFold() {
        if (mNestedScrollView == null) {
            return;
        }
        mNestedScrollView.smoothScrollTo(0, 0);
    }
}
