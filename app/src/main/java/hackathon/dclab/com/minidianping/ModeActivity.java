package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;


public class ModeActivity extends Activity {
    private Button bok = null;
    private RadioButton singleperson = null;
    private RadioButton doubleperson = null;
    private RadioButton multiperson = null;
    private RadioButton guessyoulike = null;
    private RadioButton tryfresh = null;
    private CheckBox yuecai = null;
    private CheckBox chuancai = null;
    private CheckBox xibei = null;
    private CheckBox dongnanya = null;
    private CheckBox dongbei = null;
    private CheckBox huoguo = null;
    private CheckBox xican = null;
    private CheckBox cafe = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        bok = (Button)findViewById(R.id.button_mode_ok);
        singleperson = (RadioButton)findViewById(R.id.radio_singleperson);
        doubleperson = (RadioButton)findViewById(R.id.radio_doubleperson);
        multiperson = (RadioButton)findViewById(R.id.radio_multiperson);
        guessyoulike = (RadioButton)findViewById(R.id.radio_guessyoulike);
        tryfresh = (RadioButton)findViewById(R.id.radio_tryfresh);
        chuancai = (CheckBox)findViewById(R.id.checkbox_chuancai);
        yuecai = (CheckBox)findViewById(R.id.checkbox_yuecai);
        xibei = (CheckBox)findViewById(R.id.checkbox_xibei);
        dongnanya = (CheckBox)findViewById(R.id.checkbox_dongnanya);
        dongbei = (CheckBox)findViewById(R.id.checkbox_dongbei);
        huoguo = (CheckBox)findViewById(R.id.checkbox_huoguo);
        xican = (CheckBox)findViewById(R.id.checkbox_xican);
        cafe = (CheckBox)findViewById(R.id.checkbox_cafe);

        bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
