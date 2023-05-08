package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Kejian;
import com.mobileclient.service.KejianService;
import com.mobileclient.domain.Teacher;
import com.mobileclient.service.TeacherService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class KejianEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明课件idTextView
	private TextView TV_kejianId;
	// 声明标题输入框
	private EditText ET_title;
	// 声明描述输入框
	private EditText ET_content;
	// 声明课件文件图片框控件
	private ImageView iv_kejianFile;
	private Button btn_kejianFile;
	protected int REQ_CODE_SELECT_IMAGE_kejianFile = 1;
	private int REQ_CODE_CAMERA_kejianFile = 2;
	// 声明上传老师下拉框
	private Spinner spinner_teacherObj;
	private ArrayAdapter<String> teacherObj_adapter;
	private static  String[] teacherObj_ShowText  = null;
	private List<Teacher> teacherList = null;
	/*上传老师管理业务逻辑层*/
	private TeacherService teacherService = new TeacherService();
	// 声明上传时间输入框
	private EditText ET_uploadTime;
	protected String carmera_path;
	/*要保存的课件信息*/
	Kejian kejian = new Kejian();
	/*课件管理业务逻辑层*/
	private KejianService kejianService = new KejianService();

	private int kejianId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.kejian_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑课件信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_kejianId = (TextView) findViewById(R.id.TV_kejianId);
		ET_title = (EditText) findViewById(R.id.ET_title);
		ET_content = (EditText) findViewById(R.id.ET_content);
		iv_kejianFile = (ImageView) findViewById(R.id.iv_kejianFile);
		/*单击图片显示控件时进行图片的选择*/
		iv_kejianFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(KejianEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_kejianFile);
			}
		});
		btn_kejianFile = (Button) findViewById(R.id.btn_kejianFile);
		btn_kejianFile.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_kejianFile.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_kejianFile);  
			}
		});
		spinner_teacherObj = (Spinner) findViewById(R.id.Spinner_teacherObj);
		// 获取所有的上传老师
		try {
			teacherList = teacherService.QueryTeacher(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int teacherCount = teacherList.size();
		teacherObj_ShowText = new String[teacherCount];
		for(int i=0;i<teacherCount;i++) { 
			teacherObj_ShowText[i] = teacherList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		teacherObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, teacherObj_ShowText);
		// 设置图书类别下拉列表的风格
		teacherObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_teacherObj.setAdapter(teacherObj_adapter);
		// 添加事件Spinner事件监听
		spinner_teacherObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				kejian.setTeacherObj(teacherList.get(arg2).getTeacherNo()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_teacherObj.setVisibility(View.VISIBLE);
		ET_uploadTime = (EditText) findViewById(R.id.ET_uploadTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		kejianId = extras.getInt("kejianId");
		/*单击修改课件按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取标题*/ 
					if(ET_title.getText().toString().equals("")) {
						Toast.makeText(KejianEditActivity.this, "标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_title.setFocusable(true);
						ET_title.requestFocus();
						return;	
					}
					kejian.setTitle(ET_title.getText().toString());
					/*验证获取描述*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(KejianEditActivity.this, "描述输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					kejian.setContent(ET_content.getText().toString());
					if (!kejian.getKejianFile().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						KejianEditActivity.this.setTitle("正在上传图片，稍等...");
						String kejianFile = HttpUtil.uploadFile(kejian.getKejianFile());
						KejianEditActivity.this.setTitle("图片上传完毕！");
						kejian.setKejianFile(kejianFile);
					} 
					/*验证获取上传时间*/ 
					if(ET_uploadTime.getText().toString().equals("")) {
						Toast.makeText(KejianEditActivity.this, "上传时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_uploadTime.setFocusable(true);
						ET_uploadTime.requestFocus();
						return;	
					}
					kejian.setUploadTime(ET_uploadTime.getText().toString());
					/*调用业务逻辑层上传课件信息*/
					KejianEditActivity.this.setTitle("正在更新课件信息，稍等...");
					String result = kejianService.UpdateKejian(kejian);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    kejian = kejianService.GetKejian(kejianId);
		this.TV_kejianId.setText(kejianId+"");
		this.ET_title.setText(kejian.getTitle());
		this.ET_content.setText(kejian.getContent());
		byte[] kejianFile_data = null;
		try {
			// 获取图片数据
			kejianFile_data = ImageService.getImage(HttpUtil.BASE_URL + kejian.getKejianFile());
			Bitmap kejianFile = BitmapFactory.decodeByteArray(kejianFile_data, 0, kejianFile_data.length);
			this.iv_kejianFile.setImageBitmap(kejianFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < teacherList.size(); i++) {
			if (kejian.getTeacherObj().equals(teacherList.get(i).getTeacherNo())) {
				this.spinner_teacherObj.setSelection(i);
				break;
			}
		}
		this.ET_uploadTime.setText(kejian.getUploadTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_kejianFile  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_kejianFile.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_kejianFile.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_kejianFile.setImageBitmap(booImageBm);
				this.iv_kejianFile.setScaleType(ScaleType.FIT_CENTER);
				this.kejian.setKejianFile(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_kejianFile && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_kejianFile.setImageBitmap(bm); 
				this.iv_kejianFile.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			kejian.setKejianFile(filename); 
		}
	}
}
