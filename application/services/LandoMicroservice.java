package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.LandoFinished;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;
    private static Diary instanceD= Diary.getInstance();

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent eve)->{
            try {
                Thread.sleep(duration);
                complete(eve, true);
                sendBroadcast(new LandoFinished());
               instanceD.setLandoTerminate(System.currentTimeMillis());
                terminate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Main.LeiaTimer.countDown();
    }
}

