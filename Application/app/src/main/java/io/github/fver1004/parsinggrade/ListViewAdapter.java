package io.github.fver1004.parsinggrade;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
//커스텀리스트뷰를 위한 어댑터
public class ListViewAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ListData> mListData = new ArrayList<ListData>();//All of ListDatas

    public ListViewAdapter() {    }
    public ListViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView[] mDate;
        mDate = new TextView[21];
        Integer[] tVid = {R.id.att1,R.id.att2,R.id.att3,R.id.att4,R.id.att5,R.id.att6,R.id.att7,R.id.att8,R.id.att9,R.id.att10,R.id.att11,R.id.att12,R.id.att13,R.id.att14,R.id.att15,R.id.att16,R.id.att17,R.id.att18,R.id.att19,R.id.att20,R.id.att21};

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            for(int i=0;i<21;i++)
                mDate[i] = (TextView) convertView.findViewById(tVid[i]);

        }else{
            for(int i=0;i<21;i++)
                mDate[i] = (TextView) convertView.findViewById(tVid[i]);
        }

        ListData mData = mListData.get(position);//리스트 데이터중 하나를 뽑아서 임시 저장

        Log.d("asdff",mData.arrays.length+""+mDate.length);
        for(int i=0;i<mData.arrays.length;i++)//
            mDate[i].setText(mData.arrays[i]);

        return convertView;
    }


    public void addItem(String[] datas){
        ListData addInfo = null;
        addInfo = new ListData();
        addInfo.arrays = datas;

        mListData.add(addInfo);
    }
}

//class ViewHolder {
//    public TextView[] mDate = new TextView[21];
//}