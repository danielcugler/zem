package zup.test;

import java.sql.SQLException;

import zup.model.utils.HibernateUtil;

public class ConectionPoolTest {
	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 20000; i++) {
				HibernateUtil.getSession();
				HibernateUtil.closeSession();
			}
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void test() throws InterruptedException{
		try {
			int totalTime= 0;
			for(int rep=0;rep<10;rep++){
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 200000; i++) {
				HibernateUtil.getSession().close();				
			}
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			//System.out.println(elapsedTime);
			totalTime+=elapsedTime;
			Thread.sleep(100);
			}
			System.out.println(totalTime/10);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
