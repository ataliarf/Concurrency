package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.LandoFinished;
import bgu.spl.mics.application.messages.LeiaFinished;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private static Diary instanceD= Diary.getInstance();

    public HanSoloMicroservice() {
        super("Han");
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(LeiaFinished.class,(LeiaFinished bro)->{
            instanceD.setHanSoloFinish(System.currentTimeMillis());
        });

        subscribeEvent(AttackEvent.class, (AttackEvent eve)->{
        Ewoks.getInstance().acquire(eve.getSerials());
        try {
            Thread.sleep(eve.getDuration());
            complete(eve, true);
            Main.DeactivationTimer.countDown();
            Ewoks.getInstance().release(eve.getSerials());
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
        Main.LeiaTimer.countDown();

        subscribeBroadcast(LandoFinished.class,(LandoFinished bro)->{
            terminate();
            instanceD.setHanSoloTerminate(System.currentTimeMillis());
        });
    }
}
