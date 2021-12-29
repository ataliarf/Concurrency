package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
    private Future[] futures;
    private Future[] deactivation;
    private static Diary instanceD= Diary.getInstance();


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
        futures=new Future[attacks.length];
        deactivation=new Future[1];
    }

    @Override
    protected void initialize() {
       try{
            Main.LeiaTimer.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0; i<attacks.length; i++){
            sendEvent(new AttackEvent(attacks[i].getDuration(), attacks[i].getSerials()));
        }

        sendBroadcast(new LeiaFinished());

        try{
            Main.DeactivationTimer.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendEvent(new DeactivationEvent());

        try{
            Main.BombTimer.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
    }
        sendEvent(new BombDestroyerEvent());

        subscribeBroadcast(LandoFinished.class,(LandoFinished bro)->{
            terminate();
            instanceD.setLeiaTerminate(System.currentTimeMillis());
        });
    }
}

