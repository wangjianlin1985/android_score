package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Score;
import com.mobileclient.service.ScoreService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.domain.Course;
import com.mobileclient.service.CourseService;
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

public class ScoreEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明成绩idTextView
	private TextView TV_scoreId;
	// 声明学生下拉框
	private Spinner spinner_studentObj;
	private ArrayAdapter<String> studentObj_adapter;
	private static  String[] studentObj_ShowText  = null;
	private List<Student> studentList = null;
	/*学生管理业务逻辑层*/
	private StudentService studentService = new StudentService();
	// 声明课程下拉框
	private Spinner spinner_courseObj;
	private ArrayAdapter<String> courseObj_adapter;
	private static  String[] courseObj_ShowText  = null;
	private List<Course> courseList = null;
	/*课程管理业务逻辑层*/
	private CourseService courseService = new CourseService();
	// 声明课程成绩输入框
	private EditText ET_courseScore;
	// 声明学生评价输入框
	private EditText ET_evaluate;
	// 声明添加时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的成绩信息*/
	Score score = new Score();
	/*成绩管理业务逻辑层*/
	private ScoreService scoreService = new ScoreService();

	private int scoreId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.score_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑成绩信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_scoreId = (TextView) findViewById(R.id.TV_scoreId);
		spinner_studentObj = (Spinner) findViewById(R.id.Spinner_studentObj);
		// 获取所有的学生
		try {
			studentList = studentService.QueryStudent(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int studentCount = studentList.size();
		studentObj_ShowText = new String[studentCount];
		for(int i=0;i<studentCount;i++) { 
			studentObj_ShowText[i] = studentList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		studentObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj_ShowText);
		// 设置图书类别下拉列表的风格
		studentObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj.setAdapter(studentObj_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				score.setStudentObj(studentList.get(arg2).getStudentNo()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj.setVisibility(View.VISIBLE);
		spinner_courseObj = (Spinner) findViewById(R.id.Spinner_courseObj);
		// 获取所有的课程
		try {
			courseList = courseService.QueryCourse(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int courseCount = courseList.size();
		courseObj_ShowText = new String[courseCount];
		for(int i=0;i<courseCount;i++) { 
			courseObj_ShowText[i] = courseList.get(i).getCourseName();
		}
		// 将可选内容与ArrayAdapter连接起来
		courseObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseObj_ShowText);
		// 设置图书类别下拉列表的风格
		courseObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseObj.setAdapter(courseObj_adapter);
		// 添加事件Spinner事件监听
		spinner_courseObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				score.setCourseObj(courseList.get(arg2).getCourseNo()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseObj.setVisibility(View.VISIBLE);
		ET_courseScore = (EditText) findViewById(R.id.ET_courseScore);
		ET_evaluate = (EditText) findViewById(R.id.ET_evaluate);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		scoreId = extras.getInt("scoreId");
		/*单击修改成绩按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取课程成绩*/ 
					if(ET_courseScore.getText().toString().equals("")) {
						Toast.makeText(ScoreEditActivity.this, "课程成绩输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseScore.setFocusable(true);
						ET_courseScore.requestFocus();
						return;	
					}
					score.setCourseScore(Float.parseFloat(ET_courseScore.getText().toString()));
					/*验证获取学生评价*/ 
					if(ET_evaluate.getText().toString().equals("")) {
						Toast.makeText(ScoreEditActivity.this, "学生评价输入不能为空!", Toast.LENGTH_LONG).show();
						ET_evaluate.setFocusable(true);
						ET_evaluate.requestFocus();
						return;	
					}
					score.setEvaluate(ET_evaluate.getText().toString());
					/*验证获取添加时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(ScoreEditActivity.this, "添加时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					score.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传成绩信息*/
					ScoreEditActivity.this.setTitle("正在更新成绩信息，稍等...");
					String result = scoreService.UpdateScore(score);
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
	    score = scoreService.GetScore(scoreId);
		this.TV_scoreId.setText(scoreId+"");
		for (int i = 0; i < studentList.size(); i++) {
			if (score.getStudentObj().equals(studentList.get(i).getStudentNo())) {
				this.spinner_studentObj.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < courseList.size(); i++) {
			if (score.getCourseObj().equals(courseList.get(i).getCourseNo())) {
				this.spinner_courseObj.setSelection(i);
				break;
			}
		}
		this.ET_courseScore.setText(score.getCourseScore() + "");
		this.ET_evaluate.setText(score.getEvaluate());
		this.ET_addTime.setText(score.getAddTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
