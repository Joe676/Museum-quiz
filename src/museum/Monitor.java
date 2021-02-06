package museum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import communication.ICenter;
import communication.IMonitor;
import gui.MonitorWindow;
import support.CustomException;

public class Monitor implements IMonitor{
	
	private int ID;
	private List<String> names = new ArrayList<>();
	private List<Integer> scores = new ArrayList<Integer>();
	private ICenter ic;
	private MonitorWindow window;
	
	
	public Monitor(int registryPort, MonitorWindow w) {
		window = w;
		try {
			Registry reg = LocateRegistry.getRegistry("localhost", registryPort);
			ic = (ICenter) reg.lookup("Center");
			this.ID = ic.connect((IMonitor) UnicastRemoteObject.exportObject(this, 0));
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setScore(String userName, int percentageScore) throws RemoteException {
		names.add(userName);
		scores.add(percentageScore);
		window.addScore(userName, percentageScore);
	}
	
	public void disconnect() {
		try {
			ic.disconnect(this.ID);
		} catch (RemoteException | CustomException e) {
			e.printStackTrace();
		}
	}
	
}
