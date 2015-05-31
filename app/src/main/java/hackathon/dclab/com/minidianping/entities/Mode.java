package hackathon.dclab.com.minidianping.entities;

/**
 * Created by weicheng on 30/5/15.
 */
public class Mode {
    public int number = 2;
    public int type = 1;
    public int stype = 0x000000;
    public Mode(int number, int type, int stype){
        this.number = number;
        this.type = type;
        this.stype = stype;
    }
}
