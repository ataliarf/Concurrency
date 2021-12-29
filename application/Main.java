package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch LeiaTimer; //leia waits for everyone to initialize
	public static CountDownLatch DeactivationTimer; //leia wait for han and C3 and then sends to R2D2 deactivation
    public static CountDownLatch BombTimer; // Leia waits for R2S2 to finish and then send to lando Bomb
    public static long startTime;

    public static <Json> void main(String[] args) {
        startTime=System.currentTimeMillis();
        LeiaTimer= new CountDownLatch(4);
        BombTimer= new CountDownLatch(1);
        Gson gson= new Gson();
		Input input=null;
		try{
			Reader r = new FileReader(args[0]);
			input= gson.fromJson(r, Input.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Diary d= Diary.getInstance();
		Thread Lando=new Thread (new LandoMicroservice(input.getLando()));
		Thread HanSolo=new Thread (new HanSoloMicroservice());
		Thread C3PO=new Thread (new C3POMicroservice());
		Thread R2D2=new Thread (new R2D2Microservice(input.getR2D2()));
		Thread Leia=new Thread (new LeiaMicroservice(input.getAttacks()));
		Ewoks ewoks= Ewoks.getInstance();
		ewoks.addEwoks(input.getEwoks());
		DeactivationTimer= new CountDownLatch(input.getAttacks().length);
		Leia.start();
		HanSolo.start();
		C3PO.start();
		R2D2.start();
		Lando.start();
		try{
			Leia.join();
			HanSolo.join();
			C3PO.join();
			R2D2.join();
			Lando.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Gson output=new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter writer=new FileWriter(args[1]);
			output.toJson(Diary.getInstance(),writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
