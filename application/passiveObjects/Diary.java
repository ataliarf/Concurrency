package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.LeiaFinished;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private static class SingeltonHolder
    {
        private static Diary instance = new Diary();
    }
    private Diary() {
        totalAttacks = new AtomicInteger();
    }

    public static Diary getInstance() {
        return Diary.SingeltonHolder.instance;
    }

    public void setTotalAttacks() {
        totalAttacks.incrementAndGet();
    }

    public void setHanSoloFinish(long x) {
        HanSoloFinish=x;
    }

    public void setC3POFinish(long x) { C3POFinish=x; }

    public void setR2D2Deactivate(long x) {
        R2D2Deactivate=x;
    }

    public void setLeiaTerminate(long x) { LeiaTerminate=x; }

    public void setHanSoloTerminate(long x) {
        HanSoloTerminate=x;
    }

    public void setC3POTerminate(long x) {
        C3POTerminate=x;
    }

    public void setR2D2Terminate(long x) {
        R2D2Terminate=x;
    }

    public void setLandoTerminate(long x) {
        LandoTerminate=x;
    }

    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
    return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
    return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
       return C3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getLandoTerminate() {
       return LandoTerminate;
    }

    public void resetNumberAttacks() {
        totalAttacks.set(0);
    }
}
