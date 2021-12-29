package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.Collections;
import java.util.List;

public class AttackEvent implements Event<Boolean> {
	private int duration;
	private List<Integer> serials;

    public AttackEvent(int duration, List<Integer> serials){
        this.duration= duration;
        this.serials=serials;
        Collections.sort(this.serials);
    }

    public List<Integer> getSerials(){
        return serials;
    }

    public int getDuration(){
        return duration;
    }
}
