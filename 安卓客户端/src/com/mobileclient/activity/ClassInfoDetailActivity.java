package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class ClassInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明班级编号控件
	private TextView TV_classNo;
	// 声明班级名称控件
	private TextView TV_className;
	// 声明开办日期控件
	private TextView TV_bornDate;
	// 声明班主任控件
	private TextView TV_headerName;
	/* 要保存的班级信息 */
	ClassInfo classInfo = new ClassInfo(); 
	/* 班级管理业务逻辑层 */
	private ClassInfoService classInfoService = new ClassInfoService();
	private String classNo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.classinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看班级详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_classNo = (TextView) findViewById(R.id.TV_classNo);
		TV_className = (TextView) findViewById(R.id.TV_className);
		TV_bornDate = (TextView) findViewById(R.id.TV_bornDate);
		TV_headerName = (TextView) findViewById(R.id.TV_headerName);
		Bundle extras = this.getIntent().getExtras();
		classNo = extras.getString("classNo");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ClassInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    classInfo = classInfoService.GetClassInfo(classNo); 
		this.TV_classNo.setText(classInfo.getClassNo());
		this.TV_className.setText(classInfo.getClassName());
		Date bornDate = new Date(classInfo.getBornDate().getTime());
		String bornDateStr = (bornDate.getYear() + 1900) + "-" + (bornDate.getMonth()+1) + "-" + bornDate.getDate();
		this.TV_bornDate.setText(bornDateStr);
		this.TV_headerName.setText(classInfo.getHeaderName());
	} 
}
