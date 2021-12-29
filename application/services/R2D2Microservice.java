package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.LandoFinished;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    private boolean shouldStop;
    private static Diary instanceD= Diary.getInstance();

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
        shouldStop=false;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class, (DeactivationEvent eve)->{
            try {
                Thread.sleep(duration);
                complete(eve, true);
                Main.BombTimer.countDown();
                instanceD.setR2D2Deactivate(System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Main.LeiaTimer.countDown();

        subscribeBroadcast(LandoFinished.class,(LandoFinished bro)->{
            terminate();
            instanceD.setR2D2Terminate(System.currentTimeMillis());
        });
    }
}

