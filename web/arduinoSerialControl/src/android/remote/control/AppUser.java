package android.remote.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class AppUser extends Thread{
	Socket client;
	
	InputStream is;
	InputStreamReader isr;
	BufferedReader br;
	
	OutputStream os;
	PrintWriter pw;
	
	String message;
	Vector<AppUser> userlist;
	Vector<Car> carlist;
	
	StringTokenizer st;
	public AppUser() {
		
	}
	
	public AppUser(Socket client, Vector<AppUser> userlist, Vector<Car> carlist) {
		super();
		this.client = client;
		this.userlist = userlist;
		this.carlist = carlist;
		ioWork();
	}

	public void ioWork() {
		try {
			is = client.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = client.getOutputStream();
			pw = new PrintWriter(os, true);
			
			//차에게 메시지를 보내 내가 가진 차량과 같은지 확인해야 하는 과정
			/*message = br.readLine();
			System.out.println("서버가 받은 메시지: "+message);*/
			//sendMsg("*********"+message);
			System.out.println("AppUser에 들어옴");
			//userlist에 현재 접속한 사람 추가
			userlist.add(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		pw.println(msg);
	}
	
	private void filteringMsg(String msg) {
		System.out.println("서버가 받은 클라이언트의 메시지"+msg);
		st = new StringTokenizer(msg, "/");
		String protocol = st.nextToken();
		//차에게 받은 메시지 수행!!
		if(protocol.equals("user")) {
			String receive = st.nextToken();
			if(receive.equals("car")) {
				String message = st.nextToken();
				Car car = carlist.get(0);
				car.sendMsg(receive+"/"+message);
			}
		}else {
			
		}
	}
	
	public void run() {
		while(true) {
			try {
				String msg = br.readLine();
				System.out.println("차에서 받은 메시지:"+msg);
				filteringMsg(msg);
			} catch (IOException e) {
				System.out.println("차와의 연결이 불가");
				// TODO Auto-generated catch block
				//e.printStackTrace();
				try {
					is.close();
					isr.close();
					br.close();
					os.close();
					pw.close();
					client.close();
				}catch (IOException e1) {
					// TODO: handle exception
				}
			}
		}
	}
}
