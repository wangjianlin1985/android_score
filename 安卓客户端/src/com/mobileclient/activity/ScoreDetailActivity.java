package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Score;
import com.mobileclient.service.ScoreService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.domain.Course;
import com.mobileclient.service.CourseService;
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
public class ScoreDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明成绩id控件
	private TextView TV_scoreId;
	// 声明学生控件
	private TextView TV_studentObj;
	// 声明课程控件
	private TextView TV_courseObj;
	// 声明课程成绩控件
	private TextView TV_courseScore;
	// 声明学生评价控件
	private TextView TV_evaluate;
	// 声明添加时间控件
	private TextView TV_addTime;
	/* 要保存的成绩信息 */
	Score score = new Score(); 
	/* 成绩管理业务逻辑层 */
	private ScoreService scoreService = new ScoreService();
	private StudentService studentService = new StudentService();
	private CourseService courseService = new CourseService();
	private int scoreId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.score_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看成绩详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_scoreId = (TextView) findViewById(R.id.TV_scoreId);
		TV_studentObj = (TextView) findViewById(R.id.TV_studentObj);
		TV_courseObj = (TextView) findViewById(R.id.TV_courseObj);
		TV_courseScore = (TextView) findViewById(R.id.TV_courseScore);
		TV_evaluate = (TextView) findViewById(R.id.TV_evaluate);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		scoreId = extras.getInt("scoreId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ScoreDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    score = scoreService.GetScore(scoreId); 
		this.TV_scoreId.setText(score.getScoreId() + "");
		Student studentObj = studentService.GetStudent(score.getStudentObj());
		this.TV_studentObj.setText(studentObj.getName());
		Course courseObj = courseService.GetCourse(score.getCourseObj());
		this.TV_courseObj.setText(courseObj.getCourseName());
		this.TV_courseScore.setText(score.getCourseScore() + "");
		this.TV_evaluate.setText(score.getEvaluate());
		this.TV_addTime.setText(score.getAddTime());
	} 
}
