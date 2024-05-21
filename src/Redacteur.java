package tn.enis.lectred;

public class Redacteur extends Thread {
	private Monitor M;

	public Redacteur(Monitor M, String name) {
		super(name);
		this.M = M;
	}

	@Override
	public void run() {
		try {
			M.writeRequest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		M.write();
		M.endWrite();

	}

}
