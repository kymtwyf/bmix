package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import hackathon.dclab.com.minidianping.domain.Business;

/**
 * Created by weicheng on 6/6/15.
 */
public class EvaluateActivity extends Activity {
    private Business business = null;
    private SeekBar seekbar = null;
    private TextView scoreView = null;
    private TextView businessName = null;
    private TextView choose1 = null;
    private TextView choose2 = null;
    private TextView choose3 = null;
    private TextView choose4 = null;
    private int[] choose_flag = {0,0,0,0};
    private MyListener listener = null;
    private double score = 0;
    private ImageView yes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        business = (Business)getIntent().getSerializableExtra("business");

        scoreView = (TextView)findViewById(R.id.eval_pingjia_score);
        seekbar = (SeekBar)findViewById(R.id.eval_pingjia_seek);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                score = (double)i/10.0;
                scoreView.setText(""+score);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        businessName = (TextView)findViewById(R.id.eval_header);
        businessName.setText(business.name);
        choose1 = (TextView)findViewById(R.id.eval_choose_1);
        choose2 = (TextView)findViewById(R.id.eval_choose_2);
        choose3 = (TextView)findViewById(R.id.eval_choose_3);
        choose4 = (TextView)findViewById(R.id.eval_choose_4);
        choose1.setText(business.favourite_dishes.get(0).name);
        choose2.setText(business.favourite_dishes.get(1).name);
        choose3.setText(business.favourite_dishes.get(0).name);
        choose4.setText(business.favourite_dishes.get(1).name);
        listener = new MyListener();
        choose1.setOnClickListener(listener);
        choose2.setOnClickListener(listener);
        choose3.setOnClickListener(listener);
        choose4.setOnClickListener(listener);

        yes = (ImageView)findViewById(R.id.eval_yes);
        yes.setOnClickListener(listener);
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.eval_choose_1:{
                    if(choose_flag[0]==0) {
                        choose_flag[0] = 1;
                        choose1.setBackgroundColor(Color.GRAY);
                    }
                    else{
                        choose_flag[0]=0;
                        choose1.setBackgroundColor(Color.WHITE);
                    }
                    break;
                }
                case R.id.eval_choose_2:{
                    if(choose_flag[1]==0) {
                        choose_flag[1] = 1;
                        choose2.setBackgroundColor(Color.GRAY);
                    }
                    else {
                        choose_flag[1] = 0;
                        choose2.setBackgroundColor(Color.WHITE);
                    }
                    break;
                }
                case R.id.eval_choose_3:{
                    if(choose_flag[2]==0) {
                        choose_flag[2] = 1;
                        choose3.setBackgroundColor(Color.GRAY);
                    }
                    else {
                        choose_flag[2] = 0;
                        choose3.setBackgroundColor(Color.WHITE);
                    }
                    break;
                }
                case R.id.eval_choose_4:{
                    if(choose_flag[3]==0) {
                        choose_flag[3] = 1;
                        choose4.setBackgroundColor(Color.GRAY);
                    }
                    else {
                        choose_flag[3] = 0;
                        choose4.setBackgroundColor(Color.WHITE);
                    }
                    break;
                }
                case R.id.eval_yes:{
                    Intent intent = new Intent(EvaluateActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    EvaluateActivity.this.finish();
                    break;
                }
            }
        }
    }
}
