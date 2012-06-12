package com.supinfo.notetonsta.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import com.supinfo.notetonsta.model.Intervention;

public class InterventionActivity extends BaseActivity implements
		OnItemClickListener {

	Campus campus;
	Boolean error = false;
	String message;
	List<Intervention> interventions_list = new ArrayList<Intervention>();
	InterventionListAdapter adapter;
	RetrieveInterventionAsyncTask interventionAsyncTask;
	ListView lv;
	TextView emptylist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.intervention_screen);

		campus = (Campus) getIntent().getExtras().getSerializable("campus");
		titleBar.setText(campus.getName());

		lv = (ListView) findViewById(R.id.intervention_list_view);
		adapter = new InterventionListAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		emptylist = (TextView) findViewById(R.id.empty_intervention_list);
		lv.setEmptyView(emptylist);
		
		barButtonLeft.setVisibility(View.VISIBLE);
		barButtonLeft.setEnabled(true);

		barButtonLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		barButtonRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				interventionAsyncTask = new RetrieveInterventionAsyncTask();
				interventionAsyncTask.execute(campus);
			}
		});

		interventionAsyncTask = new RetrieveInterventionAsyncTask();
		interventionAsyncTask.execute(campus);
	}

	/*
	 * List View onItemClick event (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, InterventionDetailActivity.class);
		intent.putExtra("intervention", interventions_list.get(position));
		intent.putExtra("campus", campus);
		startActivity(intent);
	}

	/*
	 * 
	 * Private Class
	 */

	static class ViewHolder {
		TextView subject;
		TextView description;
		TextView from;
		TextView to;
	}

	class InterventionListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return interventions_list.size();
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
				convertView = inflater.inflate(R.layout.intervention_list_row,
						parent, false);

				holder = new ViewHolder();
				holder.subject = (TextView) convertView
						.findViewById(R.id.intervention_subject);
				holder.description = (TextView) convertView
						.findViewById(R.id.intervention_description);
				holder.from = (TextView) convertView.findViewById(R.id.from);
				holder.to = (TextView) convertView.findViewById(R.id.to);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.subject.setText(interventions_list.get(position)
					.getSubject());
			holder.description.setText(interventions_list.get(position)
					.getDescription());

			SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");

			holder.to.setText(dateFormater.format(interventions_list.get(
					position).getEndDate()));

			holder.from.setText(dateFormater.format(interventions_list.get(
					position).getStartDate()));

			return convertView;
		}

	}

	class RetrieveInterventionAsyncTask extends
			AsyncTask<Campus, Void, List<Intervention>> {

		@Override
		protected List<Intervention> doInBackground(Campus... campus) {
			List<Intervention> interventions;
			NoteTonSTA api = ((NoteTonSTAApp) getApplication()).getApi();
			interventions = api.getInterventionsForCampus(campus[0].getKey());

			if (api.hasError()) {
				error = true;
				message = api.getError();				
			}

			return interventions;
		}

		@Override
		protected void onPostExecute(List<Intervention> result) {
			
			if(error){
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
				error = false;
				message = null;
			}
			
			interventions_list = result;
			adapter.notifyDataSetChanged();
			emptylist.setText(getResources().getString(R.string.empty_intervention_list));
			progressIndicator.setVisibility(View.INVISIBLE);
			progressIndicator.setIndeterminate(false);
			barButtonRight.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			emptylist.setText("Loading. Please wait....");
			progressIndicator.setVisibility(View.VISIBLE);
			progressIndicator.setIndeterminate(true);
			barButtonRight.setEnabled(false);
		}

	}

	
}
