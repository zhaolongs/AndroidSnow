package com.studyyoun.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * 创建人： Created by  on 2020/11/28.
 * 创建时间：Created by  on 2020/11/28.
 * 页面说明：
 * 可关注公众号：我的大前端生涯   获取最新技术分享
 * 可关注网易云课堂：https://study.163.com/instructor/1021406098.htm
 * 可关注博客：https://blog.csdn.net/zl18603543572
 */
public class CustomSnowView extends View {
	public CustomSnowView(Context context) {
		this(context, null);
	}
	
	public CustomSnowView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	
	//画笔
	Paint mPaint;
	
	//保存点的集合
	List<BobbleBean> mBobbleBeanList;
	
	public CustomSnowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mPaint = new Paint();
		
		mBobbleBeanList = new ArrayList<>();
	}
	
	
	/**
	 * 1 View绘制的过程
	 * View的测量——onMeasure()
	 * View的位置确定——onLayout()
	 * View的绘制——onDraw()
	 */
	
	
	//第一步测量
	//默认的View大小
	private int mDefaultWidth = dp2px(100);
	private int mDefaultHeight = dp2px(100);
	
	//测量过后的View 的大小  也就是画布的大小
	private int mMeasureWidth = 0;
	private int mMeasureHeight = 0;
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//获取测量计算相关内容
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		
		if (widthSpecMode == MeasureSpec.EXACTLY) {
			//当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
			mMeasureWidth = widthSpecSize;
		} else {
			//指定默认大小
			mMeasureWidth = mDefaultWidth;
			if (widthSpecMode == MeasureSpec.AT_MOST) {
				mMeasureWidth = Math.min(mMeasureWidth, widthSpecSize);
			}
		}
		
		//测量计算View的高
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (heightSpecMode == MeasureSpec.EXACTLY) {
			//当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
			mMeasureHeight = heightSpecSize;
		} else {
			//指定默认大小
			mMeasureHeight = mDefaultHeight;
			if (heightSpecMode == MeasureSpec.AT_MOST) {
				mMeasureHeight = Math.min(mMeasureHeight, heightSpecSize);
			}
		}
		mMeasureHeight = mMeasureHeight - getPaddingBottom() - getPaddingTop();
		mMeasureWidth = mMeasureWidth - getPaddingLeft() - getPaddingBottom();
		//重新测量
		setMeasuredDimension(mMeasureWidth, mMeasureHeight);
	}
	
	//这里面创建 点
	Random mRandom = new Random();
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		for (int i = 0; i < mMeasureWidth / 3; i++) {
			
			BobbleBean lBobbleBean = new BobbleBean();
			
			//生成位置信息  随机
			//取值范围是 0 ~ mMeasureWidth
			int x = mRandom.nextInt(mMeasureWidth);
			int y = mRandom.nextInt(mMeasureHeight);
			
			//绘制使用的位置
			lBobbleBean.postion = new Point(x, y);
			//重置的位置
			lBobbleBean.origin = new Point(x, 0);
			//随机的半径  1 ~ 4
			lBobbleBean.radius = mRandom.nextFloat() * 3 + dp2px(1);
			//随机的速度  3 ~ 6
			lBobbleBean.speed = 1 + mRandom.nextInt(3);
			//随机透明度的白色
			lBobbleBean.color = ColorUtil.randomWhiteColor();
			mBobbleBeanList.add(lBobbleBean);
		}
		
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//绘制时重新计算位置
		for (BobbleBean lBobbleBean : mBobbleBeanList) {
			
			Point lPostion = lBobbleBean.postion;
			//在竖直方向上增加偏移
			lPostion.y+=lBobbleBean.speed;
			
			//在 x 轴方向上再微微偏移一点
			float randValue = mRandom.nextFloat() *2 -0.5f;
			lPostion.x+=randValue;
			
			//边界控制
			if(lPostion.y>mMeasureHeight){
				lPostion.y = 0;
			}
		}
		
		//先将这些点全部绘制出来
		
		for (BobbleBean lBobbleBean : mBobbleBeanList) {
			//修改画笔的颜色
			mPaint.setColor(lBobbleBean.color);
			//绘制
			// 参数一 二 圆点位置
			// 参数 三 半径
			// 参数 四 画笔
			canvas.drawCircle(lBobbleBean.postion.x, lBobbleBean.postion.y, lBobbleBean.radius, mPaint);
		}
		
		//循环刷新 10 毫秒刷新一次
		postInvalidateDelayed(10L);
		
	}
	
	//一个 dp 转 像素的计算
	private int dp2px(int dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5f);
	}
}























