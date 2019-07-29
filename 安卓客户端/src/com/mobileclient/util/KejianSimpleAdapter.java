package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.TeacherService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class KejianSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;

    public KejianSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.kejian_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_kejianId = (TextView)convertView.findViewById(R.id.tv_kejianId);
	  holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
	  holder.iv_kejianFile = (ImageView)convertView.findViewById(R.id.iv_kejianFile);
	  holder.tv_teacherObj = (TextView)convertView.findViewById(R.id.tv_teacherObj);
	  holder.tv_uploadTime = (TextView)convertView.findViewById(R.id.tv_uploadTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_kejianId.setText("课件id：" + mData.get(position).get("kejianId").toString());
	  holder.tv_title.setText("标题：" + mData.get(position).get("title").toString());
	  holder.iv_kejianFile.setImageResource(R.drawable.default_photo);
	  ImageLoadListener kejianFileLoadListener = new ImageLoadListener(mListView,R.id.iv_kejianFile);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("kejianFile"),kejianFileLoadListener);  
	  holder.tv_teacherObj.setText("上传老师：" + (new TeacherService()).GetTeacher(mData.get(position).get("teacherObj").toString()).getName());
	  holder.tv_uploadTime.setText("上传时间：" + mData.get(position).get("uploadTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_kejianId;
    	TextView tv_title;
    	ImageView iv_kejianFile;
    	TextView tv_teacherObj;
    	TextView tv_uploadTime;
    }
} 
