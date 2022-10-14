package com.yyzy.constellation.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.yyzy.constellation.R;

import java.util.Random;

public class SwipeCaptchaView extends AppCompatImageView {
    //控件的宽高
    protected int mWidth;
    protected int mHeight;

    //验证码滑块的宽高
    private int mCaptchaWidth;
    private int mCaptchaHeight;
    //验证码的左上角(起点)的x y
    private int mCaptchaX;
    @SuppressWarnings("FieldCanBeLocal")
    private int mCaptchaY;
    private Random mRandom;
    private Paint mPaint;
    //验证码 阴影、抠图的Path
    private Path mCaptchaPath;
    private PorterDuffXfermode mPorterDuffXfermode;


    //是否绘制滑块（验证失败闪烁动画用）
    private boolean isDrawMask;
    //滑块Bitmap
    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;
    //用于绘制阴影的Paint
    private Paint mMaskShadowPaint;
    //滑块背后的阴影，需要关闭硬件加速才能显示
    private Bitmap mMaskShadowBitmap;
    //滑块的位移
    private int mDragerOffset;

    //是否处于验证模式，在验证成功后 为false，其余情况为true
    private boolean isMatchMode;
    //验证的误差允许值
    private float mMatchDeviation;
    //验证失败的闪烁动画
    private ValueAnimator mFailAnim;
    //验证成功的白光一闪动画
    private boolean isShowSuccessAnim;
    private ValueAnimator mSuccessAnim;
    private Paint mSuccessPaint;//画笔
    private int mSuccessAnimOffset;//动画的offset
    private Path mSuccessPath;//成功动画 平行四边形Path


    public SwipeCaptchaView(Context context) {
        this(context, null);
    }

    public SwipeCaptchaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeCaptchaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //滑块默认尺寸
        int defaultSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        mCaptchaHeight = defaultSize;
        mCaptchaWidth = defaultSize;

        //误差值
        mMatchDeviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

        //设置的属性值
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeCaptchaView, defStyleAttr, 0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.SwipeCaptchaView_captchaHeight) {
                mCaptchaHeight = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.SwipeCaptchaView_captchaWidth) {
                mCaptchaWidth = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.SwipeCaptchaView_matchDeviation) {
                mMatchDeviation = ta.getDimension(attr, mMatchDeviation);
            }
        }
        ta.recycle();


        mRandom = new Random(System.nanoTime());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xaa000000);
        // 设置画笔遮罩滤镜
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        //滑块区域
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 实例化阴影画笔
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        mCaptchaPath = new Path();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //动画区域 会用到宽高
        createMatchAnim();
        //尺寸改变，重置图形
        post(this::createCaptcha);
    }

    //验证动画初始化区域
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void createMatchAnim() {
        mFailAnim = ValueAnimator.ofFloat(0, 1);
        mFailAnim.setDuration(100)
                .setRepeatCount(4);
        mFailAnim.setRepeatMode(ValueAnimator.REVERSE);
        //失败的时候先闪一闪动画 斗鱼是 隐藏-显示 -隐藏 -显示
        mFailAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchFailed(SwipeCaptchaView.this);
            }
        });
        mFailAnim.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            isDrawMask = !(animatedValue < 0.5f);
            invalidate();
        });

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                getResources().getDisplayMetrics());
        mSuccessAnim = ValueAnimator.ofInt(mWidth + width, 0);
        mSuccessAnim.setDuration(500);
        mSuccessAnim.setInterpolator(new FastOutLinearInInterpolator());
        mSuccessAnim.addUpdateListener(animation -> {
            mSuccessAnimOffset = (int) animation.getAnimatedValue();
            invalidate();
        });
        mSuccessAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShowSuccessAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchSuccess(SwipeCaptchaView.this);
                isShowSuccessAnim = false;
                isMatchMode = false;
            }
        });
        mSuccessPaint = new Paint();
        mSuccessPaint.setShader(new LinearGradient(0, 0, width/2*3, mHeight, new int[]{
                0x00ffffff, 0x88ffffff}, new float[]{0,0.5f},
                Shader.TileMode.MIRROR));
        //模仿斗鱼 是一个平行四边形滚动过去
        mSuccessPath = new Path();
        mSuccessPath.moveTo(0, 0);
        mSuccessPath.rLineTo(width, 0);
        mSuccessPath.rLineTo(width / 2, mHeight);
        mSuccessPath.rLineTo(-width, 0);
        mSuccessPath.close();
    }

    //生成验证码区域
    public void createCaptcha() {
        if (getDrawable() != null) {
            resetFlags();
            createCaptchaPath();
            createMask();
            invalidate();
        }
    }

    //重置一些flasg， 开启验证模式
    private void resetFlags() {
        isMatchMode = true;
    }

    //生成验证码Path
    private void createCaptchaPath() {
        //原本打算随机生成gap，后来发现 宽度/3 效果比较好，
        //int gap = mRandom.nextInt(mCaptchaWidth / 2);
        int gap = mCaptchaWidth / 3;

        //随机生成验证码阴影左上角 x y 点，
        mCaptchaX = mWidth / 2 + mRandom.nextInt(mWidth / 2 - 2 * mCaptchaWidth - gap);
        mCaptchaY = mCaptchaHeight + mRandom.nextInt(mHeight - 3 * mCaptchaHeight - gap);

        mCaptchaPath.reset();
        mCaptchaPath.lineTo(0, 0);

        //从左上角开始 绘制一个不规则的阴影
        mCaptchaPath.moveTo(mCaptchaX, mCaptchaY);//左上角

        mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + gap, mCaptchaY),
                new PointF(mCaptchaX + gap * 2, mCaptchaY),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY);//右上角
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mCaptchaWidth, mCaptchaY + gap),
                new PointF(mCaptchaX + mCaptchaWidth, mCaptchaY + gap * 2),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + mCaptchaHeight);//右下角
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth - gap, mCaptchaY + mCaptchaHeight);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mCaptchaWidth - gap, mCaptchaY + mCaptchaHeight),
                new PointF(mCaptchaX + mCaptchaWidth - gap * 2, mCaptchaY + mCaptchaHeight),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight);//左下角
        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight - gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX, mCaptchaY + mCaptchaHeight - gap),
                new PointF(mCaptchaX, mCaptchaY + mCaptchaHeight - gap * 2),
                mCaptchaPath, mRandom.nextBoolean());

        mCaptchaPath.close();
    }

    //生成滑块
    private void createMask() {
        mMaskBitmap = getMaskBitmap(((BitmapDrawable) getDrawable()).getBitmap(), mCaptchaPath);
        //滑块阴影
        mMaskShadowBitmap = mMaskBitmap.extractAlpha();
        //拖动的位移重置
        mDragerOffset = dp2px(getContext(), offset);
        //isDrawMask  绘制失败闪烁动画用
        isDrawMask = true;
    }

    //抠图
    private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {
        //以控件宽高 create一块bitmap
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        //把创建的bitmap作为画板
        Canvas mCanvas = new Canvas(tempBitmap);
        //有锯齿 且无法解决,所以换成XFermode的方法做
        //mCanvas.clipPath(mask);
        // 抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //绘制用于遮罩的圆形
        mCanvas.drawPath(mask, mMaskPaint);
        //设置遮罩模式(图像混合模式)
        mMaskPaint.setXfermode(mPorterDuffXfermode);
        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap, getImageMatrix(), mMaskPaint);
        mMaskPaint.setXfermode(null);

        return tempBitmap;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //继承自ImageView，所以Bitmap，ImageView已经帮我们draw好了。
        //我只在上面绘制和验证码相关的部分，

        //是否处于验证模式，在验证成功后 为false，其余情况为true
        if (isMatchMode) {

            //绘制滑块
            // isDrawMask  绘制失败闪烁动画用
            if (null != mMaskBitmap && null != mMaskShadowBitmap && isDrawMask) {

                //修改canvas的density，屏幕适配会导致density不一致
                canvas.setDensity(mMaskBitmap.getDensity());

                //首先绘制验证码阴影，即待填充部分
                if (mCaptchaPath != null) {
                    //待填充阴影，此处为填充模式
                    canvas.drawPath(mCaptchaPath, mPaint);

                    //描绘白边
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(Color.WHITE);
                    mPaint.setStrokeWidth(1);
                    canvas.drawPath(mCaptchaPath, mPaint);

                    //重置填充模式
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(0xaa000000);
                }


                // 先绘制阴影
                canvas.drawBitmap(mMaskShadowBitmap, -mCaptchaX + mDragerOffset, 0, mMaskShadowPaint);
                canvas.drawBitmap(mMaskBitmap, -mCaptchaX + mDragerOffset, 0, null);
            }

            //验证成功，白光扫过的动画，这一块动画感觉不完美，有提高空间
            if (isShowSuccessAnim) {
                canvas.translate(mSuccessAnimOffset, 0);
                canvas.drawPath(mSuccessPath, mSuccessPaint);
            }
        }
    }


    /**
     * 校验
     */
    public void matchCaptcha() {
        if (null != onCaptchaMatchCallback && isMatchMode) {
            //这里验证逻辑，是通过比较，拖拽的距离 和 验证码起点x坐标。 默认3dp以内算是验证成功。
            if (Math.abs(mDragerOffset - mCaptchaX) < mMatchDeviation) {
                //成功的动画
                mSuccessAnim.start();
            } else {
                mFailAnim.start();
            }
        }
    }

    /**
     * 重置验证码滑动距离,(一般用于验证失败)
     */
    public void resetCaptcha() {
        mDragerOffset = dp2px(getContext(), offset);
        invalidate();
    }

    /**
     * 最大可滑动值
     * @return 最大可滑动值，限制了右边空余值
     */
    public int getMaxSwipeValue() {
        //返回控件宽度
        return mWidth - mCaptchaWidth - 2 * dp2px(getContext(), offset);
    }

    /**
     * 设置当前滑动值
     * @param value 当前滑动值，加上初始位移值
     */
    public void setCurrentSwipeValue(int value) {
        mDragerOffset = dp2px(getContext(), offset) + value;
        invalidate();
    }

    /**
     * 验证码验证的回调
     */
    public interface OnCaptchaMatchCallback {
        void matchSuccess(SwipeCaptchaView swipeCaptchaView);
        void matchFailed(SwipeCaptchaView swipeCaptchaView);
    }

    private OnCaptchaMatchCallback onCaptchaMatchCallback;

    public void setOnCaptchaMatchCallback(OnCaptchaMatchCallback onCaptchaMatchCallback) {
        this.onCaptchaMatchCallback = onCaptchaMatchCallback;
    }


    //留着吧，人写出来也不容易
    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0;
            int flag;//旋转系数
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }
        }
    }

    //初始偏移值，右边空余值，我这里代码里面写9和外部的seekbar比较适配
    public final int offset = 9;
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
