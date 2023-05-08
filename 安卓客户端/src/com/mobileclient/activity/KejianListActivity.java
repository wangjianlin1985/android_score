package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Kejian;
import com.mobileclient.service.KejianService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.KejianSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class KejianListActivity extends Activity {
	KejianSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int kejianId;
	/* 课件操作业务逻辑层对象 */
	KejianService kejianService = new KejianService();
	/*保存查询参数条件的课件对象*/
	private Kejian queryConditionKejian;

	private MyProgressDialog dialog; //进度条	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.kejian_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(KejianListActivity.this, KejianQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("课件查询列表");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(KejianListActivity.this, KejianAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionKejian = (Kejian)extras.getSerializable("queryConditionKejian");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionKejian = null;
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//在子线程中进行下载数据操作
				list = getDatas();
				//发送消失到handler，通知主线程下载完成
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new KejianSimpleAdapter(KejianListActivity.this, list,
	        					R.layout.kejian_list_item,
	        					new String[] { "kejianId","title","kejianFile","teacherObj","uploadTime" },
	        					new int[] { R.id.tv_kejianId,R.id.tv_title,R.id.iv_kejianFile,R.id.tv_teacherObj,R.id.tv_uploadTime,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// 添加长按点击
		lv.setOnCreateContextMenuListener(kejianListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int kejianId = Integer.parseInt(list.get(arg2).get("kejianId").toString());
            	Intent intent = new Intent();
            	intent.setClass(KejianListActivity.this, KejianDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("kejianId", kejianId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener kejianListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑课件信息"); 
			menu.add(0, 1, 0, "删除课件信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑课件信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取课件id
			kejianId = Integer.parseInt(list.get(position).get("kejianId").toString());
			Intent intent = new Intent();
			intent.setClass(KejianListActivity.this, KejianEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("kejianId", kejianId);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除课件信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取课件id
			kejianId = Integer.parseInt(list.get(position).get("kejianId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(KejianListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = kejianService.DeleteKejian(kejianId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询课件信息 */
			List<Kejian> kejianList = kejianService.QueryKejian(queryConditionKejian);
			for (int i = 0; i < kejianList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("kejianId", kejianList.get(i).getKejianId());
				map.put("title", kejianList.get(i).getTitle());
				/*byte[] kejianFile_data = ImageService.getImage(HttpUtil.BASE_URL+ kejianList.get(i).getKejianFile());// 获取图片数据
				BitmapFactory.Options kejianFile_opts = new BitmapFactory.Options();  
				kejianFile_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(kejianFile_data, 0, kejianFile_data.length, kejianFile_opts); 
				kejianFile_opts.inSampleSize = photoListActivity.computeSampleSize(kejianFile_opts, -1, 100*100); 
				kejianFile_opts.inJustDecodeBounds = false; 
				try {
					Bitmap kejianFile = BitmapFactory.decodeByteArray(kejianFile_data, 0, kejianFile_data.length, kejianFile_opts);
					map.put("kejianFile", kejianFile);
				} catch (OutOfMemoryError err) { }*/
				map.put("kejianFile", HttpUtil.BASE_URL+ kejianList.get(i).getKejianFile());
				map.put("teacherObj", kejianList.get(i).getTeacherObj());
				map.put("uploadTime", kejianList.get(i).getUploadTime());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
