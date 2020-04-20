package zup.utils;

import zup.business.BroadcastMessageBusiness;

public class Testes {

	 public boolean removeExpired(){
		 BroadcastMessageBusiness bmb = new BroadcastMessageBusiness();
		 
		return false;
		 
	 }
	public static void main(String[] args) {
		  // intervalo de um dia entre execução
		  final long timeInterval = 24*60*60*1000;
		  Runnable runnable = new Runnable() {
		  public void run() {
		    while (true) {
		      // ------- code for task to run
		      System.out.println("Hello !!");
		      // ------- ends here
		      try {
		       Thread.sleep(timeInterval);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		      }
		    }
		  };
		  Thread thread = new Thread(runnable);
		  thread.start();
		  }

}
