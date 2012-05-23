package com.supinfo.notetonsta.android;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.notetonsta.android.util.Rating;
import com.supinfo.notetonsta.api.NoteTonSTA;
import com.supinfo.notetonsta.model.Campus;
import com.supinfo.notetonsta.model.Intervention;

public class InterventionDetailActivity extends BaseActivity {

	Intervention intervention;
	Campus campus;
	Rating rating;
	TextView subject;
	TextView description;
	TextView campusName;
	TextView from;
	TextView to;
	TextView countMark;
	RatingBar speakerMark;
	RatingBar slideMark;
	RatingBar globalMark;
	Button evaluate;
	RetrieveRating retrieveRatinAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.intervention_detail_screen);

		campus = (Campus) getIntent().getExtras().getSerializable("campus");
		intervention = (Intervention) getIntent().getExtras().getSerializable(
				"intervention");
						
		titleBar.setText("Intervention Details");
		
		barButtonRight.setVisibility(View.INVISIBLE);
		
		barButtonLeft.setImageDrawable(getResources().getDrawable(
				R.drawable.navigate_left_icon));
		barButtonLeft.setEnabled(true);
		barButtonLeft.setVisibility(View.VISIBLE);

		barButtonLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		subject = (TextView) findViewById(R.id.detail_subject);
		description = (TextView) findViewById(R.id.detail_description);
		campusName = (TextView) findViewById(R.id.detail_campus);	
		from = (TextView) findViewById(R.id.detail_from);
		to = (TextView) findViewById(R.id.detail_to);
		countMark = (TextView) findViewById(R.id.countMark);
		
		updateUI();
		
		speakerMark = (RatingBar) findViewById(R.id.speakerMark);
		slideMark = (RatingBar) findViewById(R.id.slideMark);
		globalMark = (RatingBar) findViewById(R.id.globalMark);

		evaluate = (Button) findViewById(R.id.button_evaluate);
		evaluate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InterventionDetailActivity.this,
						InterventionEvaluateActivity.class);
				intent.putExtra("intervention", intervention);
				startActivityForResult(intent, 0);
			}
		});

		retrieveRatinAsyncTask = new RetrieveRating();
		retrieveRatinAsyncTask.execute(intervention);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(resultCode){
		case NoteTonSTA.RESULT_CODE_EVALUATION : 
			if (data.getBooleanExtra("evaluated", false)){
				RetrieveRating ratingTask = new RetrieveRating();				
				UpdateIntervention updater = new UpdateIntervention();
				
				updater.execute(intervention);
				ratingTask.execute(intervention);
			}
			break;
		case NoteTonSTA.RESULT_CODE_BACK:
			break;
		}
	}

	public void updateUI() {
		subject.setText(intervention.getSubject());
		
		description.setText(intervention.getDescription());
		
		campusName.setText(campus.getName());
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");		
		
		from.setText(dateFormater.format(intervention.getStartDate()));
		
		to.setText(dateFormater.format(intervention.getEndDate()));
		
		countMark.setText(String.valueOf(intervention.getCountMark()));
	}

	class RetrieveRating extends AsyncTask<Intervention, Void, Rating> {

		@Override
		protected Rating doInBackground(Intervention... interventions) {
			NoteTonSTA api = ((NoteTonSTAApp) getApplication()).getApi();
			Rating rating = api.getRatingForIntervention(interventions[0]);

			if (api.hasError()) {
				error = true;
				message = api.getError();
				
			}

			return rating;
		}

		@Override
		protected void onPostExecute(Rating result) {
			if (error) {
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				error = false;
				message = null;
			}

			rating = result;

			// speakerMark.setRating(Float.parseFloat(rating.get("avgspeaker")));
			// slideMark.setRating(Float.parseFloat(rating.get("avgslide")));
			// globalMark.setRating(Float.parseFloat(rating.get("globalavg")));

			

			try {
				speakerMark.setRating((float) rating.getAvgSpeaker());

				slideMark.setRating((float) rating.getAvgSlide());

				globalMark.setRating((float) rating.getGlobalAvg());

			} catch (Exception e) {
				speakerMark.setRating(0);
				slideMark.setRating(0);
				globalMark.setRating(0);
			}

			progressIndicator.setVisibility(View.INVISIBLE);
			progressIndicator.setIndeterminate(false);
			barButtonRight.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			
			if (error){
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
				error = false;
				message = null;
			}
			
			progressIndicator.setVisibility(View.VISIBLE);
			progressIndicator.setIndeterminate(true);			
		}

	}

	class UpdateIntervention extends
			AsyncTask<Intervention, Void, Intervention> {

		@Override
		protected Intervention doInBackground(Intervention... interventions) {
			NoteTonSTA api = ((NoteTonSTAApp) getApplication()).getApi();
			Intervention updatedIntervention = api
					.findIntervention(interventions[0].getKey());

			if (api.hasError()) {
				error = true;
				message = api.getError();
				return intervention;
			} else {
				return updatedIntervention;
			}

		}

		@Override
		protected void onPostExecute(Intervention result) {
			if (error){
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
				error = false;
				message = null;
			}
			
			intervention = result;
			updateUI();
			progressIndicator.setVisibility(View.INVISIBLE);
			progressIndicator.setIndeterminate(false);			
		}

		@Override
		protected void onPreExecute() {
			progressIndicator.setVisibility(View.VISIBLE);
			progressIndicator.setIndeterminate(true);
			barButtonRight.setEnabled(false);
		}

	}

}
