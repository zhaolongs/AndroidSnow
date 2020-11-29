# AndroidSnow
Android 雪花飘落动画效果

>在码农的世界里，优美的应用体验，来源于程序员对细节的处理以及自我要求的境界，年轻人也是忙忙碌碌的码农中一员，每天、每周，都会留下一些脚印，就是这些创作的内容，有一种执着，就是不知为什么，如果你迷茫，不妨来瞅瞅码农的轨迹。

* [优美的音乐节奏带你浏览这个效果的编码过程](https://www.zhihu.com/zvideo/1315733902364315648)
* [坚持每一天，是每个有理想青年的追求](https://www.ixigua.com/6900018293596226059/)
*  [追寻年轻人的脚步，也许你的答案就在这里](https://www.bilibili.com/video/BV1ha4y1W7o1/)

本文章实现的效果如下图所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201128105150457.gif#pic_center)

#### 1 首先是雪花的定义
用来保存雪花的一些基本属性

```java
public class BobbleBean {
	//位置
	Point postion;
	//初始位置
	Point origin;
	//颜色
	int color;
	//运动的速度
	int speed;
	//半径
	float radius;
}

```

#### 2 自定义 View 创建
然后我们创建一个自定义 View 用来绘制雪花效果，在这个自定义View的构造函数中创建一个画笔，同时创建一个保存雪花的集合：

```java
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
}
```

#### 3 绘制
绘制讲究三步【[精通Android自定义View(二)View绘制三部曲](https://biglead.blog.csdn.net/article/details/88373862)】

##### 3.1 第一步就是测量
通过测量要得出当前画布的精确尺寸
```java
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
```


```java
	//一个 dp 转 像素的计算
	private int dp2px(int dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5f);
	}
```

##### 3.2 第二步就是 排版
当然这个功能主要用于 ViewGroup 中有多个 View时，在这里我们来根据画布的尺寸来随机生成雪花点的坐标信息

```java

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
```

##### 3.3 第三步就是绘制

循环绘制，每次绘制时就将雪花的 纵坐标添加偏移量 就出现了向下落的动画效果

```java
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
```

####  4  第四步就是使用
在布局文件中引用这个自定义View

```java
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#000000"
    tools:context=".MainActivity">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="fitXY"
        android:src="@mipmap/bg_snow" />

    <com.studyyoun.demo.CustomSnowView
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201130074226602.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3psMTg2MDM1NDM1NzI=,size_16,color_FFFFFF,t_70#pic_center)



【x1】微信公众号的每日提醒  随时随记 每日积累 随心而过 文章底部扫码关注

[【x2】各种系列的视频教程 免费开源  关注 你不会迷路](https://www.ixigua.com/home/3662978423/?source=pgc_author_name&list_entrance=pSeriesWatch)

[【x3】系列文章 百万 Demo 随时 复制粘贴 使用](https://biglead.blog.csdn.net/article/details/93532582)

[【x4】简短的视频不一样的体验](https://www.zhihu.com/zvideo/1312058739554992128)

[【x5】必须有源码](https://github.com/zhaolongs/AndroidSnow)



***

不局限于思维，不局限语言限制，才是编程的最高境界。

以小编的性格，肯定是要录制一套视频的，随后会上传

 有兴趣 你可以关注一下  [西瓜视频 --- 早起的年轻人](https://www.toutiao.com/c/user/token/MS4wLjABAAAAYMrKikomuQJ4d-cPaeBqtAK2cQY697Pv9xIyyDhtwIM/)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201031094959816.gif#pic_center)

