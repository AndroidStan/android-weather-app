package com.weather.android.adapter;

import com.weather.android.R;
import com.weather.android.application.WeatherApplication;
import com.weather.android.util.room.CityDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HomeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<CityDetails> citiesDetails;
	private Context context;
	
	public HomeAdapter(Context context, List<CityDetails> citiesDetails){
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.citiesDetails = citiesDetails;
	}
	
	public int getCount() {
		return citiesDetails.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	} 

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			
			convertView = inflater.inflate(R.layout.list_item_text,null);
			
			// create a view holder
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.text);
			
			convertView.setTag(holder);
			
		}
		else {
			// get the ViewHolder back for fast access
			holder = (ViewHolder) convertView.getTag();
		}
		
		//String homeListItemText = WeatherApplication.get

		// bind the data with the holder
		String listItemString = citiesDetails.get(position).getName()
								+ " - "
								+ citiesDetails.get(position).getStatename()
								+ " - "
				        		+ citiesDetails.get(position).getZipcode();

		holder.name.setText(listItemString);

		return convertView;
	}
	
	static class ViewHolder{
		TextView name;
	}

}
