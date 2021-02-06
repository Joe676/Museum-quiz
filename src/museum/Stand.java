package museum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import communication.ICenter;
import communication.IStand;
import gui.StandWindow;
import support.CustomException;
import support.Description;

public class Stand implements IStand {
	
	private int ID;
	private Description description;
	private StandWindow window;
	private ICenter ic;
	
	public Stand(int registryPort, StandWindow w) {
		window = w;
		try {
			Registry reg = LocateRegistry.getRegistry("localhost", registryPort);
			ic = (ICenter) reg.lookup("Center");
			this.ID = ic.connect((IStand) UnicastRemoteObject.exportObject(this, 0));
		} catch (RemoteException|NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setContent(Description d) throws RemoteException {
		this.description = d;
		window.updateDescription(this.description.description);
	}

	@Override
	public int getId() throws RemoteException {
		return this.ID;
	}
	
	public void disconnect() {
		try {
			ic.disconnect(this.ID);
		} catch (RemoteException | CustomException e) {
			e.printStackTrace();
		}
	}

}
