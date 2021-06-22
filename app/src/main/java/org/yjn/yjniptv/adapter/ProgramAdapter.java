package org.yjn.yjniptv.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.yjn.yjniptv.bean.Program;

import java.util.HashMap;

public class ProgramAdapter extends BaseAdapter {
    private HashMap<Integer, Program> programHashMap = new HashMap<Integer, Program>(){{
        put(1,new Program("CCTV-1综合","rtmp://58.200.131.2:1935/livetv/cctv1"));
        put(2,new Program("CCTV-2财经","rtmp://58.200.131.2:1935/livetv/cctv2"));
        put(3,new Program("CCTV-3综艺","rtmp://58.200.131.2:1935/livetv/cctv3"));
        put(4,new Program("CCTV-4中文国际","rtmp://58.200.131.2:1935/livetv/cctv4"));
        put(5,new Program("CCTV-5体育","rtmp://58.200.131.2:1935/livetv/cctv5"));
        put(6,new Program("CCTV-6电影","rtmp://58.200.131.2:1935/livetv/cctv6"));
        put(7,new Program("CCTV-7军事农业","rtmp://58.200.131.2:1935/livetv/cctv7"));
        put(8,new Program("CCTV-8电视剧","rtmp://58.200.131.2:1935/livetv/cctv8"));
        put(9,new Program("CCTV-9记录","rtmp://58.200.131.2:1935/livetv/cctv9"));
        put(10,new Program("CCTV-10科教","rtmp://58.200.131.2:1935/livetv/cctv10"));
        put(11,new Program("CCTV-11戏曲","rtmp://58.200.131.2:1935/livetv/cctv11"));
        put(12,new Program("CCTV-12社会与法","rtmp://58.200.131.2:1935/livetv/cctv12"));
        put(13,new Program("CCTV-13新闻","rtmp://58.200.131.2:1935/livetv/cctv13"));
        put(14,new Program("CCTV-14少儿","rtmp://58.200.131.2:1935/livetv/cctv14"));
        put(15,new Program("CCTV-15音乐","rtmp://58.200.131.2:1935/livetv/cctv15"));
    }};


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
