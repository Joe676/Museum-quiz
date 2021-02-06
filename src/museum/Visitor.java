package museum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import communication.ICenter;
import gui.VisitorWindow;
import support.Answer;
import support.CustomException;
import support.Question;

public class Visitor{
	private int ID;
	private String name;
//	private int score = 0;
	private ICenter ic;
	private Question[] questions;
//	private Answer[] answers;
	private boolean[] results;
	private VisitorWindow window;
	
	public Visitor(int registryPort, String name, VisitorWindow w) {
		this.window = w;
		this.name = name;
		try {
			Registry reg = LocateRegistry.getRegistry("localhost", registryPort);
			ic = (ICenter) reg.lookup("Center");
			this.ID = ic.signIn(name);
			this.questions = ic.getQuestions();
			String[] q = new String[this.questions.length];
			for(int i = 0; i<this.questions.length; i++) {
				q[i] = this.questions[i].question;
			}
			window.setQuestions(q);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean[] checkAnswers(String[] a) {
		Answer[] aa = new Answer[a.length];
		for(int i = 0; i<a.length; i++) {
			aa[i]=new Answer();
			aa[i].answer = a[i];
		}
		try {
			this.results = ic.checkAnswers(this.ID, aa);
			return this.results;
		} catch (RemoteException | CustomException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public void signOut() {
		try {
			ic.signOut(this.ID);
		} catch (RemoteException | CustomException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
}
