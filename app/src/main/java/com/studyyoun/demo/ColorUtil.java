package com.studyyoun.demo;

import android.graphics.Color;

import java.util.Random;

/*
 * 创建人： Created by  on 2020/11/27.
 * 创建时间：Created by  on 2020/11/27.
 * 页面说明：
 * 可关注公众号：我的大前端生涯   获取最新技术分享
 * 可关注网易云课堂：https://study.163.com/instructor/1021406098.htm
 * 可关注博客：https://blog.csdn.net/zl18603543572
 */
public class ColorUtil {
	/**
	 * 生成随机颜色
	 *
	 * @return
	 */
	public static int randomColor() {
		Random random = new Random();
		int red = random.nextInt(200);
		int green = random.nextInt(200);
		int blue = random.nextInt(200);
		return Color.rgb(red, green, blue);
	}
	
	/**
	 * 生成随机透明度的白色
	 * @return
	 */
	public static int randomWhiteColor() {
		Random random = new Random();
		int a = random.nextInt(200);
		
		return Color.argb(a, 255, 255,255);
	}
}
