package com.tsroad.map.route;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amap.api.services.core.PoiItem;
import com.tsroad.map.R;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by T.s.road on 2015/4/13 0013.
 */

public class RouteSearchAdapter extends BaseAdapter {
	private Context context;
	private List<PoiItem> poiItems = null;
	private LayoutInflater mInflater;

	public RouteSearchAdapter(Context context, List<PoiItem> poiItems) {
		this.context = context;
		this.poiItems = poiItems;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return poiItems.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.poi_result_list, null);
		}

		TextView PoiName = ((TextView) convertView.findViewById(R.id.poiName));
		TextView poiAddress = (TextView) convertView
				.findViewById(R.id.poiAddress);
		PoiName.setText(poiItems.get(position).getTitle());
		String address = null;
		if (poiItems.get(position).getSnippet() != null) {
			address = poiItems.get(position).getSnippet();
		} else {
			address = "中国";
		}
		poiAddress.setText("地址:" + address);
		return convertView;
	}

}
