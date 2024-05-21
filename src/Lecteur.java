package tn.enis.lectred;

public class Lecteur extends Thread {
	private Monitor M;

	public Lecteur(Monitor M, String name) {
		super(name);
		this.M = M;
	}

	@Override
	public void run() {
		try {
			M.readRequest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		M.read();
		M.endRead();

	}

}
