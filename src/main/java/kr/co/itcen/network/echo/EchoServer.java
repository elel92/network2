package kr.co.itcen.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int PORT = 8000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. 소켓 생성
			serverSocket = new ServerSocket();
			
			//2. 바인딩
			InetAddress inetAddress = InetAddress.getLocalHost();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);
			
			//3. accept : 클라이언트로 부터 연결 요청 기다림
			
			while(true) {
				Socket socket = serverSocket.accept();
				new EchoServerReceiveThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String log) {
		System.out.println("[Echo Server] " + log);
	}
}
