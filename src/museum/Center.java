package museum;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import communication.ICenter;
import communication.IDesigner;
import communication.IMonitor;
import communication.IStand;
import support.Answer;
import support.CustomException;
import support.Question;

public class Center implements ICenter {

	private Map<Integer, IMonitor> imonitors = new HashMap<Integer, IMonitor>();
	private Map<Integer, String> visitors = new HashMap<Integer, String>();
	private Map<Integer, Boolean> vAnswered = new HashMap<Integer, Boolean>();
	private Map<Integer, IStand> istands = new HashMap<Integer, IStand>();
	private Map<Integer, IDesigner> idesigners = new HashMap<Integer, IDesigner>();
	private Question[] questions;
	private Answer[] answers;
	
	private static int identifier = 0;
	
	public Center(int registryPort) {
		try {
			Registry reg = LocateRegistry.createRegistry(registryPort);
			reg.rebind("Center", UnicastRemoteObject.exportObject(this, 0));
			System.out.println("Center is ready");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Provide port for a registry: ");
		Scanner sc = new Scanner(System.in);
		new Center(sc.nextInt());
		sc.close();
	}
	
	@Override
	public int signIn(String visitorName) throws RemoteException {
		visitors.put(identifier, visitorName);
		vAnswered.put(identifier, false);
		System.out.println("User "+visitorName+" signed in");
		return identifier++;
	}

	@Override
	public void signOut(int visitorId) throws RemoteException, CustomException {
		if(!visitors.containsKey(visitorId))throw new CustomException("Visitor with this ID is not signed in");
		System.out.println("User number " + visitorId+" signed off");
		visitors.remove(visitorId);
	}

	@Override
	public Question[] getQuestions() throws RemoteException {
		System.out.println("Sending questions");
		return questions;
	}

	@Override
	public boolean[] checkAnswers(int userId, Answer[] a) throws RemoteException, CustomException {
		if(!visitors.containsKey(userId))throw new CustomException("Visitor with this ID is not signed in");
		if(vAnswered.get(userId))throw new CustomException("This user has already answered");
		System.out.println("Checking answers by user number "+userId);
		boolean[] out = new boolean[a.length];
		int n = 0;
		for(int i = 0; i<a.length; i++) {
			if(a[i].answer.equals(answers[i].answer)) {
				out[i] = true;
				n++;
			}
		}
		n*=100;
		int p = n/a.length ;
		for(IMonitor im: imonitors.values()) {
			im.setScore(visitors.get(userId), p);
		}
		
		return out;
	}

	@Override
	public void addQA(Question[] q, Answer[] a) throws RemoteException, CustomException {
		if(!visitors.isEmpty())throw new CustomException("Unable to change QA, there are visitors signed in");
		System.out.println("Adding Questions & Answers");
		questions = q;
		answers = a;
	}

	@Override
	public IStand[] getStands() throws RemoteException {
		System.out.println("Sending stand stubs");
		return istands.values().toArray(new IStand[istands.values().size()]);
	}

	@Override
	public int connect(IDesigner id) throws RemoteException {
		System.out.println("Designer Connected");
		idesigners.put(identifier, id);
		return identifier++;
	}

	@Override
	public int connect(IStand is) throws RemoteException {
		System.out.println("Stand connected");
		istands.put(identifier, is);
		for(IDesigner id: idesigners.values()) {
			id.notify(identifier, true);
		}
		return identifier++;
	}

	@Override
	public int connect(IMonitor im) throws RemoteException {
		System.out.println("Monitor connected");
		imonitors.put(identifier, im);
		return identifier++;
	}

	@Override
	public void disconnect(int identifier) throws RemoteException, CustomException {
		System.out.println("Disconnecting "+identifier);
		if(imonitors.containsKey(identifier)) {
			imonitors.remove(identifier);
		}else if(istands.containsKey(identifier)) {
			istands.remove(identifier);
			
			for(IDesigner id: idesigners.values()) {
				id.notify(identifier, false);
			}
			
		}else if(idesigners.containsKey(identifier)) {
			idesigners.remove(identifier);
		}else throw new CustomException("Object with this identifier is not connected");
	}

}
