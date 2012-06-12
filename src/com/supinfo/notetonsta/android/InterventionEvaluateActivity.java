package com.supinfo.notetonsta.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.supinfo.notetonsta.api.NoteTonSTA;
import com.supinfo.notetonsta.model.Intervention;
import com.supinfo.notetonsta.model.Mark;

public class InterventionEvaluateActivity extends BaseActivity {
	EditText idbooster;
	EditText comment;	
	RatingBar q1;
	RatingBar q2;
	RatingBar q3;
	RatingBar q4;
	RatingBar q5;
	RatingBar q6;
	Intervention intervention;
	
	@Override
	public void onBackPressed() {				
		setResult(NoteTonSTA.RESULT_CODE_BACK);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.evaluation_screen);
		
		intervention = (Intervention) getIntent().getExtras().getSerializable("intervention");
		
		titleBar.setText("Evaluate");
		barButtonLeft.setImageDrawable(getResources().getDrawable(R.drawable.navigate_left_icon));
		barButtonLeft.setEnabled(true);
		barButtonLeft.setVisibility(View.VISIBLE);
		
		barButtonLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				setResult(NoteTonSTA.RESULT_CODE_BACK);
				finish();
			}
		});
		
		barButtonRight.setImageDrawable(getResources().getDrawable(R.drawable.actions_ok_icon));
		barButtonRight.setVisibility(View.INVISIBLE);
		barButtonRight.setEnabled(false);
		barButtonRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Mark mark = new Mark();
				mark.setIdbooster(idbooster.getText().toString());
				mark.setSpeakerMark((double) ((q1.getRating() + q2.getRating() + q3.getRating()) / 3));
				mark.setSlideMark((double) ((q4.getRating() + q5.getRating() + q6.getRating()) / 3));
				mark.setIntervention(intervention.getKey());
				mark.setComent(comment.getText().toString());
				PostEvaluation postEvaluationAsynctask = new PostEvaluation();
				postEvaluationAsynctask.execute(mark);
			}
		});
		
		idbooster = (EditText) findViewById(R.id.idbooster);
		idbooster.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(v.getId() == R.id.idbooster){
					if (idbooster.getText().length() == 0){
						barButtonRight.setVisibility(View.INVISIBLE);
						barButtonRight.setEnabled(false);
					}else{
						barButtonRight.setVisibility(View.VISIBLE);
						barButtonRight.setEnabled(true);
					}
				}
				return false;
			}
		});
		
		comment =  (EditText) findViewById(R.id.comment);
		
		q1 = (RatingBar) findViewById(R.id.q1);
		q2 = (RatingBar) findViewById(R.id.q2);
		q3 = (RatingBar) findViewById(R.id.q3);
		
		q4 = (RatingBar) findViewById(R.id.q4);
		q5 = (RatingBar) findViewById(R.id.q5);
		q6 = (RatingBar) findViewById(R.id.q6);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putFloat("q1", q1.getRating());
		outState.putFloat("q2", q2.getRating());
		outState.putFloat("q3", q3.getRating());
		outState.putFloat("q4", q4.getRating());
		outState.putFloat("q5", q5.getRating());
		outState.putFloat("q6", q6.getRating());
		outState.putCharSequence("idbooster", idbooster.getText());
		outState.putCharSequence("comment", comment.getText());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		q1.setRating(savedInstanceState.getFloat("q1",0));
		q2.setRating(savedInstanceState.getFloat("q2",0));
		q3.setRating(savedInstanceState.getFloat("q3",0));
		q4.setRating(savedInstanceState.getFloat("q4",0));
		q5.setRating(savedInstanceState.getFloat("q5",0));
		q6.setRating(savedInstanceState.getFloat("q6",0));
		
		idbooster.setText(savedInstanceState.getCharSequence("idbooster"));
		comment.setText(savedInstanceState.getCharSequence("comment"));
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	class PostEvaluation extends AsyncTask<Mark, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Mark... marks) {
			NoteTonSTA api = ((NoteTonSTAApp)getApplication()).getApi();
			
			Boolean result = api.evaluate(marks[0]);
			
			if(api.hasError()){
				error = true;
				message = api.getError();
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressIndicator.setVisibility(View.INVISIBLE);
			progressIndicator.setIndeterminate(false);
			barButtonRight.setEnabled(true);
			barButtonLeft.setEnabled(true);
			
			if(error){
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
				error = false;
				message = null;
			}else{
				Toast.makeText(getBaseContext(), "Evaluation complete !", Toast.LENGTH_LONG).show();
				setResult(NoteTonSTA.RESULT_CODE_EVALUATION, getIntent().putExtra("evaluated", true));
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			progressIndicator.setVisibility(View.VISIBLE);
			progressIndicator.setIndeterminate(true);
			barButtonRight.setEnabled(false);
			barButtonLeft.setEnabled(false);
		}
	}
}
