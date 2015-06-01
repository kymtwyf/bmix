package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import hackathon.dclab.com.minidianping.entities.Mode;


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
    private CheckBox others = null;
    private int personnumber = 1;
    private int type = 1;
    private int stype=0;

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
        others = (CheckBox)findViewById(R.id.checkbox_others);
        if(MainActivity.mode==null) {
            doubleperson.setChecked(true);
            guessyoulike.setChecked(true);
        }
        else{
            setButton(MainActivity.mode);
        }

        bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(singleperson.isChecked()){
                    personnumber = 1;
                }
                if(doubleperson.isChecked()){
                    personnumber = 2;
                }
                if(multiperson.isChecked()){
                    personnumber = 3;
                }
                if(guessyoulike.isChecked()){
                    type = 1;
                }
                if(tryfresh.isChecked()){
                    type = 2;
                }
                if(chuancai.isChecked()){
                    stype |= 1;
                }
                if(yuecai.isChecked()){
                    stype |= 1;
                }
                if(xibei.isChecked()){
                    int temp = 1 << 2;
                    stype |= temp;
                }
                if(dongnanya.isChecked()){
                    int temp = 1 << 3;
                    stype |= temp;
                }
                if(dongbei.isChecked()){
                    int temp = 1 << 4;
                    stype |= temp;
                }
                if(huoguo.isChecked()){
                    int temp = 1 << 5;
                    stype |= temp;
                }
                if(xican.isChecked()){
                    int temp = 1 << 6;
                    stype |= temp;
                }
                if(cafe.isChecked()){
                    int temp = 1 << 7;
                    stype |= temp;
                }
                if(others.isChecked()){
                    int temp = 1 <<8;
                    stype |= temp;
                }
                Mode newmode = new Mode(personnumber,type,stype);
                MainActivity.mode = newmode;
                Intent intent = new Intent(ModeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setButton(Mode mode){
        if(mode.number==1){
            singleperson.setChecked(true);
        }
        if(mode.number==2){
            doubleperson.setChecked(true);
        }
        if(mode.number==3){
            multiperson.setChecked(true);
        }
        if(mode.type==1){
            guessyoulike.setChecked(true);
        }
        if(mode.type==2){
            tryfresh.setChecked(true);
        }
        int stype = mode.stype;
        if((stype & 1) == 1){
            chuancai.setChecked(true);
        }
        if(((stype &(1<<1)) >>1 )==1){
            yuecai.setChecked(true);
        }
        if(((stype &(1<<2)) >> 2)==1){
            xibei.setChecked(true);
        }
        if(((stype &(1<<3)) >> 3)==1){
            dongnanya.setChecked(true);
        }
        if(((stype &(1<<4)) >> 4)==1){
            dongbei.setChecked(true);
        }
        if(((stype &(1<<5)) >> 5)==1){
            huoguo.setChecked(true);
        }
        if(((stype &(1<<6)) >> 6)==1){
            xican.setChecked(true);
        }
        if(((stype &(1<<7)) >> 7)==1){
            cafe.setChecked(true);
        }
        if(((stype &(1<<8)) >> 8)==1){
            others.setChecked(true);
        }
    }
}
