package bgu.spl.mics;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;
import java.util.*;
import java.util.concurrent.*;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> queue1 = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>> typesQueue = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>> broadcastList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event, Future> futureMap = new ConcurrentHashMap<>();
	private static Diary instanceD= Diary.getInstance();
	private final Object firstLock= new Object();
	private final Object secondLock= new Object();

	private static class SingeltonHolder
	{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
	}

	public static MessageBusImpl getInstance(){
		return MessageBusImpl.SingeltonHolder.instance;
	}

	@Override
	public  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (type) {
			if (typesQueue.get(type) == null) {
				LinkedBlockingQueue<MicroService> l = new LinkedBlockingQueue<>();
				try {
					l.put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				typesQueue.put(type, l);
			} else {
				try {
					typesQueue.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public  void subscribeBroadcast (Class<? extends Broadcast> type, MicroService m) {
		synchronized (type) {
			if (broadcastList.get(type) == null) {
				LinkedBlockingQueue<MicroService> l = new LinkedBlockingQueue<>();
				try {
					l.put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				broadcastList.put(type, l);
			} else {
				try {
					broadcastList.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (secondLock){
			futureMap.get(e).resolve(result);
			if (e instanceof AttackEvent) {
				instanceD.setTotalAttacks();
			}
		}
	}

	@Override
	public  void sendBroadcast(Broadcast b) {
		synchronized (firstLock) {
				if (broadcastList.containsKey(b.getClass())) {
					LinkedBlockingQueue<MicroService> l = broadcastList.get(b.getClass());
					for (MicroService m : l) {
						if (queue1 != null && queue1.containsKey(m)) {
							try {
								queue1.get(m).put(b);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

	public  <T> Future<T> sendEvent(Event<T> e) {
		synchronized (secondLock) {
			if (!(typesQueue.containsKey(e.getClass())) || (typesQueue.get(e.getClass())).isEmpty())
				return null;
			else {
				MicroService m;
				try {
					synchronized (firstLock){
					m = typesQueue.get(e.getClass()).take();
					queue1.get(m).put(e);
					typesQueue.get(e.getClass()).put(m);}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				Future<T> f = new Future<>();
				futureMap.put(e, f);
				return f;
			}
		}
	}

	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> l = new LinkedBlockingQueue<>();
		queue1.put(m, l);
	}

	@Override
	public void unregister(MicroService m) {
		synchronized (firstLock) {
			synchronized (secondLock) {
				queue1.remove(m);
				Set<Class<? extends Event>> eventsSet = typesQueue.keySet();
				for (Class<? extends Event> e : eventsSet) {
					typesQueue.get(e).remove(m);
				}
				Set<Class<? extends Broadcast>> keys = broadcastList.keySet();
				for (Class<? extends Broadcast> b : keys) {
					broadcastList.get(b).remove(m);
				}
			}
		}
	}

	@Override
	public   Message awaitMessage(MicroService m) throws InterruptedException {
			Message output=null;
				try {
				output= queue1.get(m).take();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
		return output;
		}
	}



