package ntou.cs.java.Sunshystar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

/*
 * ChatRoom 定義進入聊天室後與Server建立連線
 * 
 * @author Ricky
 */
public class ChatRoom extends Sunshystar {
	static BufferedReader receiveMsgReader;
	static PrintWriter sendMsgWriter;
	static Vector<String> listData = new Vector<String>();
	
	ChatRoom() {
		try {
        	// 建立Socket
			socket = new Socket(serverIPAddress, port);
			
			// 接收Server端的訊息
			InputStream in = socket.getInputStream();
			receiveMsgReader = new BufferedReader(new InputStreamReader(in, "Unicode"));
			
			// 送給Server端的訊息
			OutputStream out = socket.getOutputStream();
			sendMsgWriter = new PrintWriter(new OutputStreamWriter(out, "Unicode"), true);
		}
		catch (UnknownHostException e) {
			System.out.println(e.toString());
		}
		catch (IOException e) {			
			System.out.println(e.toString());
		}
		
	}
}
