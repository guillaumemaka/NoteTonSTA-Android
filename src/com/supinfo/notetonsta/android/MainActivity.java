package com.supinfo.notetonsta.android;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.notetonsta.api.NoteTonSTA;
import com.supinfo.notetonsta.model.Campus;

public class MainActivity extends BaseActivity implements OnItemClickListener {
	private final String ActivityTitle = "Campus List";
	Boolean error = false;
	String message;
	private List<Campus> campus_list = new ArrayList<Campus>();
	private CampusListAdapter adapter;
	private RetrieveCampusListAsyncTask campusAsynctask;
	ListView lv;
	TextView emptylist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main);

		progressIndicator.setVisibility(View.INVISIBLE);

		lv = (ListView) findViewById(R.id.campus_list_view);
		adapter = new CampusListAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		emptylist = (TextView) findViewById(R.id.empty_campus_list);
		lv.setEmptyView(emptylist);

		titleBar.setText(ActivityTitle);

		barButtonLeft.setEnabled(false);
		barButtonLeft.setVisibility(View.INVISIBLE);

		barButtonRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				barButtonRight.setEnabled(false);
				barButtonRight.setClickable(false);
				campusAsynctask = new RetrieveCampusListAsyncTask();
				campusAsynctask.execute();
			}
		});

		NoteTonSTAApp app = (NoteTonSTAApp) getApplication();

		if (app.getSettings().contains("campus_list")) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				this.campus_list = mapper.readValue(app.getSettings()
						.getString("campus_list", "[]"),
						new TypeReference<ArrayList<Campus>>() {
						});
			} catch (Exception e) {

			}

			adapter.notifyDataSetChanged();
		} else {
			if (isOnline()) {
				campusAsynctask = new RetrieveCampusListAsyncTask();
				campusAsynctask.execute();
			}else{
				emptylist.setText("Network not available");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	/*
	 * ListView OnItemClick Event (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent interventionListActivity = new Intent(this,
				InterventionActivity.class);
		interventionListActivity.putExtra("campus", campus_list.get(position));
		startActivity(interventionListActivity);
	}

	/*
	 * CustomListViewAdapter
	 */

	static class ViewHolder {
		TextView text1;
	}

	class CampusListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return campus_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(
						android.R.layout.simple_list_item_1, parent, false);

				holder = new ViewHolder();

				holder.text1 = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text1.setText(campus_list.get(position).getName());

			return convertView;
		}

	}

	class RetrieveCampusListAsyncTask extends
			AsyncTask<Void, Void, List<Campus>> {

		@Override
		protected void onPostExecute(List<Campus> result) {
			NoteTonSTAApp app = (NoteTonSTAApp) getApplication();
			SharedPreferences prefs = app.getSettings();

			if (error) {
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				error = false;
				message = null;
			} else {
				prefs.edit()
						.putString("campus_list",
								NoteTonSTA.getJsonRepresentation()).commit();
			}

			campus_list = result;
			adapter.notifyDataSetChanged();
			emptylist.setText(getResources().getString(
					R.string.empty_campus_list));
			progressIndicator.setVisibility(View.INVISIBLE);
			progressIndicator.setIndeterminate(false);
			barButtonRight.setEnabled(true);
		}

		@Override
		protected List<Campus> doInBackground(Void... params) {
			NoteTonSTAApp app = (NoteTonSTAApp) getApplication();
			NoteTonSTA api = app.getApi();
			List<Campus> campus;
			campus = api.getAllCampus();

			if (api.hasError()) {
				error = true;
				message = api.getError();
			}

			return campus;
		}

		@Override
		protected void onPreExecute() {
			emptylist.setText("Loading. Please wait....");
			progressIndicator.setVisibility(View.VISIBLE);
			progressIndicator.setIndeterminate(true);
		}

	}

}