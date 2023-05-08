package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Kejian;
import com.mobileclient.service.KejianService;
import com.mobileclient.domain.Teacher;
import com.mobileclient.service.TeacherService;
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
public class KejianDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明课件id控件
	private TextView TV_kejianId;
	// 声明标题控件
	private TextView TV_title;
	// 声明描述控件
	private TextView TV_content;
	// 声明课件文件图片框
	private ImageView iv_kejianFile;
	// 声明上传老师控件
	private TextView TV_teacherObj;
	// 声明上传时间控件
	private TextView TV_uploadTime;
	/* 要保存的课件信息 */
	Kejian kejian = new Kejian(); 
	/* 课件管理业务逻辑层 */
	private KejianService kejianService = new KejianService();
	private TeacherService teacherService = new TeacherService();
	private int kejianId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.kejian_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看课件详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_kejianId = (TextView) findViewById(R.id.TV_kejianId);
		TV_title = (TextView) findViewById(R.id.TV_title);
		TV_content = (TextView) findViewById(R.id.TV_content);
		iv_kejianFile = (ImageView) findViewById(R.id.iv_kejianFile); 
		TV_teacherObj = (TextView) findViewById(R.id.TV_teacherObj);
		TV_uploadTime = (TextView) findViewById(R.id.TV_uploadTime);
		Bundle extras = this.getIntent().getExtras();
		kejianId = extras.getInt("kejianId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				KejianDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    kejian = kejianService.GetKejian(kejianId); 
		this.TV_kejianId.setText(kejian.getKejianId() + "");
		this.TV_title.setText(kejian.getTitle());
		this.TV_content.setText(kejian.getContent());
		byte[] kejianFile_data = null;
		try {
			// 获取图片数据
			kejianFile_data = ImageService.getImage(HttpUtil.BASE_URL + kejian.getKejianFile());
			Bitmap kejianFile = BitmapFactory.decodeByteArray(kejianFile_data, 0,kejianFile_data.length);
			this.iv_kejianFile.setImageBitmap(kejianFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Teacher teacherObj = teacherService.GetTeacher(kejian.getTeacherObj());
		this.TV_teacherObj.setText(teacherObj.getName());
		this.TV_uploadTime.setText(kejian.getUploadTime());
	} 
}
