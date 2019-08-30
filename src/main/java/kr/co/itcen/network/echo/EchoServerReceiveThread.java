package kr.co.itcen.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress(); // 블로킹
		String remoteHostAdress = inetRemoteSocketAddress.getAddress().getHostAddress();
		int remoteHostPort = inetRemoteSocketAddress.getPort();
		
		EchoServer.log("connected from client[" + remoteHostAdress + ":" + remoteHostPort + "]");
		
		try {
			//IOStream 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			while(true) {
				//5. 데이터 읽기(수신)
				String data = br.readLine();
				
				if(data == null) { //정상종료 : remote socket이 close()
					EchoServer.log("closed by client");
					break;
				}
				
				//6. 데이트 쓰기(송신)
				EchoServer.log("received: " + data);
				pw.println(data);
			}
		} catch(SocketException e) {
			EchoServer.log("abnormal closed by client");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
