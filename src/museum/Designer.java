package museum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import communication.ICenter;
import communication.IDesigner;
import communication.IStand;
import gui.DesignerWindow;
import support.Answer;
import support.CustomException;
import support.Description;
import support.Question;

public class Designer implements IDesigner {
	
	private int ID;
	private Map<Integer, IStand> istands = new HashMap<Integer, IStand>();
	private ICenter ic;
	private DesignerWindow window;
	private boolean newStand = false;
	
	
	public Designer(int registryPort, DesignerWindow w) {
		window = w;
		try {
			Registry reg = LocateRegistry.getRegistry("localhost", registryPort);
			ic = (ICenter) reg.lookup("Center");
			ID = ic.connect((IDesigner) UnicastRemoteObject.exportObject(this, 0));
			IStand[] s = ic.getStands();
			if(s!=null && s.length>0) {
				for(IStand is:s) {
					if(is!=null)istands.put(is.getId(), is);
				}
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void notify(int standId, boolean isConnected) throws RemoteException {
		System.out.println(standId+" notified");
//		if(isConnected) {
//			IStand[] s = ic.getStands();
//			for(IStand is:s) {
//				System.out.println(is.getId()+" wutwut");
//				istands.put(is.getId(), is);
//			}
//		}
//		else {
//		
//		}
		
		// Niemozliwe jest poprawne odebranie nowych standow z danymi interfejsami
		// Center jest zablokowany dopoki Designer nie skonczy tej funkcji
		// Stand dostanie ID dopiero w momencie zakonczenia funkcji connect w Center, ktora oczekuje na skonczenie tej funkcji
		// Powoduje to nadpisanie standow
		if(!isConnected) {
			istands.remove(standId);
			window.showStands();
		}else {
			newStand = true;
		}
	}
	
	public boolean addQA(Question[] q, Answer[] a) {
		try {
			ic.addQA(q, a);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void setContent(int standId, String description) {
		
		Description d = new Description();
		d.description = description;
		try {
			istands.get(standId).setContent(d);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			ic.disconnect(this.ID);
		} catch (RemoteException | CustomException e) {
			e.printStackTrace();
		}
	}


	public int getId() {
		return this.ID;
	}


	public boolean addQA(List<String> qs, List<String> as) {
		Question[] q = new Question[qs.size()];
		Answer[] a = new Answer[as.size()];
		
		for(int i = 0; i<qs.size(); i++) {
			q[i] = new Question();
			q[i].question = qs.get(i);
			a[i] = new Answer();
			a[i].answer = as.get(i);
		}
		return addQA(q, a);
	}
	
	public List<Integer> getStandIds(){
//		System.out.println(istands.keySet());
		if(newStand ) {
			IStand[] s;
			try {
				s = ic.getStands();
				if(s!=null && s.length>0) {
					for(IStand is:s) {
						if(is!=null)istands.put(is.getId(), is);
					}
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newStand = false;
		}
		return new ArrayList<Integer>(istands.keySet());
	}

}
