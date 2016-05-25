package kr.ac.snu.ads.facetracking;

public interface FaceTrackingRunnerInterface extends Runnable{
	public void run();
	public void setRepository(TraceRepository repo);
}
