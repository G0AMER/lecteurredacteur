import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private int nbRedacteur = 0;
	private int nbLecteur = 0;
	private int redacteurActif = 0;
	private final Lock lock = new ReentrantLock();
	private final Condition okToRead = lock.newCondition();
	private final Condition okToWrite = lock.newCondition();

	void readRequest() throws InterruptedException {
		lock.lock();
		try {
			while (nbRedacteur > 0) {
				System.out.println(Thread.currentThread().getName() + " waiting to read");
				okToRead.await();
				System.out.println(Thread.currentThread().getName() + " end waiting to read");
			}
			nbLecteur++;
			okToRead.signal(); // reveille en cascade
			System.out.println(Thread.currentThread().getName() + " Ready to Read");
		} finally {
			lock.unlock();
		}
	}

	void read() {
		System.out.println(Thread.currentThread().getName() + " Reading ...");
	}

	void endRead() {
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " end reading");
			nbLecteur--;
			if (nbLecteur == 0)
				okToWrite.signal();
		} finally {
			lock.unlock();
		}
	}

	void writeRequest() throws InterruptedException {
		lock.lock();
		try {
			nbRedacteur++;
			while (redacteurActif == 1 || nbLecteur > 0) {
				System.out.println(Thread.currentThread().getName() + " waiting to write");
				okToWrite.await();
				System.out.println(Thread.currentThread().getName() + " end waiting for writing");
			}
			redacteurActif = 1;
			System.out.println(Thread.currentThread().getName() + " Ready to Write");
		} finally {
			lock.unlock();
		}
	}

	void write() {
		System.out.println(Thread.currentThread().getName() + " writing");
	}

	void endWrite() {
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " end writing");
			nbRedacteur--;
			redacteurActif = 0;
			if (nbLecteur > 0)
				okToRead.signal();
			else
				okToWrite.signal();

		} finally {
			lock.unlock();
		}
	}

}
