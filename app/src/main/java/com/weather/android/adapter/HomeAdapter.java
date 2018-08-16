package com.weather.android.adapter;

import com.weather.android.R;
import com.weather.android.util.room.CityDetails;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
//dividers can be used this way:
//https://www.bignerdranch.com/blog/a-view-divided-adding-dividers-to-your-recyclerview-with-itemdecoration/
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

	public interface OnItemClickListener {
		void onItemClick(CityDetails item);
		void onItemLongClick(CityDetails item);
	}

	private List<CityDetails> citiesDetails;
	private OnItemClickListener listener;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView name;
		public ViewHolder(LinearLayout v) {
			super(v);
			name = (TextView) v.findViewById(R.id.text);
		}

		public void bind(final CityDetails item, final OnItemClickListener listener) {
			//name.setText(item.name);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					listener.onItemClick(item);
				}
			});
			itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override public boolean onLongClick(View v) {
					listener.onItemLongClick(item);
					return true;
				}
			});
		}
	}
	
	public HomeAdapter(List<CityDetails> citiesDetails, OnItemClickListener listener){
		this.citiesDetails = citiesDetails;
		this.listener = listener;
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
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

	// Create new views (invoked by the layout manager)
	@Override
	public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		LinearLayout listItemView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, null);

		ViewHolder vh = new ViewHolder(listItemView);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element

		String listItemString = citiesDetails.get(position).getName()
				+ " - "
				+ citiesDetails.get(position).getStatename()
				+ " - "
				+ citiesDetails.get(position).getZipcode();

		holder.name.setText(listItemString);
		holder.bind(citiesDetails.get(position), listener);
	}
}
