package kr.ac.snu.ads.facetracking;

public interface FaceDetectingRunnerInterface extends Runnable{
	public void run();
	public void setRepository(VieweesRepository repo);
}
