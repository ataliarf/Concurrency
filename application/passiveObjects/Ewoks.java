package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private List<Ewok> ewoksList;

    private static class SingeltonHolder
    {
        private static Ewoks instance = new Ewoks();
    }
    private Ewoks(){
      ewoksList=new LinkedList<Ewok>();
    }

    public static Ewoks getInstance(){
        return Ewoks.SingeltonHolder.instance;
    }

    public   void acquire(List<Integer> l){
        for(int i=0; i<l.size(); i++){
            ewoksList.get(l.get(i)-1).acquire();
        }
    }

    public  void release(List<Integer> l){
        for(int i=0; i<l.size(); i++){
            ewoksList.get(l.get(i)-1).release();
        }
    }

    public void addEwoks(int numOfEwoks){ // in cell 0 we have ewok with serial 1!!!
        for (int i=0; i<numOfEwoks; i++)
        ewoksList.add(new Ewok(i+1));
    }

}
