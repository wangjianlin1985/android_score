package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Score;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.domain.Course;
import com.mobileclient.service.CourseService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class ScoreQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
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
	/*查询过滤条件保存到这个对象中*/
	private Score queryConditionScore = new Score();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.score_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置成绩查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_studentObj = (Spinner) findViewById(R.id.Spinner_studentObj);
		// 获取所有的学生
		try {
			studentList = studentService.QueryStudent(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int studentCount = studentList.size();
		studentObj_ShowText = new String[studentCount+1];
		studentObj_ShowText[0] = "不限制";
		for(int i=1;i<=studentCount;i++) { 
			studentObj_ShowText[i] = studentList.get(i-1).getName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		studentObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj_ShowText);
		// 设置学生下拉列表的风格
		studentObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj.setAdapter(studentObj_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionScore.setStudentObj(studentList.get(arg2-1).getStudentNo()); 
				else
					queryConditionScore.setStudentObj("");
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
		courseObj_ShowText = new String[courseCount+1];
		courseObj_ShowText[0] = "不限制";
		for(int i=1;i<=courseCount;i++) { 
			courseObj_ShowText[i] = courseList.get(i-1).getCourseName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		courseObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseObj_ShowText);
		// 设置课程下拉列表的风格
		courseObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseObj.setAdapter(courseObj_adapter);
		// 添加事件Spinner事件监听
		spinner_courseObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionScore.setCourseObj(courseList.get(arg2-1).getCourseNo()); 
				else
					queryConditionScore.setCourseObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseObj.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionScore", queryConditionScore);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
