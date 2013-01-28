



import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.io.PrintWriter;

import java.net.Socket;

/*

 * 負責處理連接server端的socket，每個client都有各自的ChangeThread在監聽

 * 用來監聽client傳送過來的訊息。

 * 

 * 

 * @author Arvin

 */

public class ChatThread extends Thread {

private Socket socket = null;

   private ChatServer server;

   private BufferedReader br;

   private PrintWriter pw;

   private int ID;

   public ChatThread(Socket socket1,ChatServer server) {

	   this.socket = socket1;

	   this.server = server;	

	   this.setName(socket.getInetAddress().getHostAddress());

	   try {

	    InputStream in = socket.getInputStream();      //接收客戶端的訊息

		br = new BufferedReader(new InputStreamReader(in,"Unicode"));  

		   OutputStream out = socket.getOutputStream(); //送出訊息給客戶端

		   pw = new PrintWriter(new OutputStreamWriter(out, "Unicode"),true);

	} catch (IOException e) {		

		e.printStackTrace();

	}

	   

   }

	

   //讀取客戶端來的訊息

	public void run() {

		int out = 0;

		try {

			String msg = br.readLine();

			if(msg==null){

				server.removeClient(this);

			}

			while(msg != null) {

				if(msg.equalsIgnoreCase("out")){

					server.removeClient(this);

					out=1;

				}

				else if(msg.equalsIgnoreCase("change")){

					//將client轉到waitting

						if( ChatServer.waitting.contains(this)){

							//do nothing

						}

						else{

					server.changeClient(this);

						}

				}

				else{

				server.sendMessage(msg,getNumber()); //將訊息轉給server去發送

				}

				if(out==0){

				msg = br.readLine();

				}

				else break;

			}

		} catch (IOException e) {			

			e.printStackTrace();

			

		}

		

	}

	

	

	public void sendMessage(String msg) {	

		pw.println(msg);

	}

	public void setNumber(int x){

		ID = x ;

	}

	public int getNumber(){

		return ID;

	}

	public boolean isClose(){

		return socket.isClosed();

	}

	public void release() {

		if(br != null ) {

			try {

				br.close();

			} catch (IOException e) {		

				e.printStackTrace();

			}

		}

		

		if(pw != null) {

			pw.close();

		}

		

		if(socket != null && ! socket.isClosed()) {

			try {

				socket.close();

			} catch (IOException e) {				

				e.printStackTrace();

			}

		}

		

	}

}

