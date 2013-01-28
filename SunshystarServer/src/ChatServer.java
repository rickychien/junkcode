



import java.awt.BorderLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.IOException;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.ArrayList;

import java.util.Random;

import java.util.Vector;



import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

/*

 * 系統的server端，負責處理各個client端的登入登出，以及訊息的傳遞。

 * 

 * @author Arvin

 */

@SuppressWarnings("serial")

public class ChatServer extends JFrame {

   JPanel jp = new JPanel();

   JPanel jp2 = new JPanel();

   JButton jbStart = new JButton("啓動server"); 

   JLabel  jl = new JLabel("server的監聽端口為8000");

   JList list = new JList();

   JLabel jonline = new JLabel("線上人數：0");

   JScrollPane jsp = new JScrollPane(list);   

   //--------------------

   private  ServerSocket serverSocket;

   Vector<String> listData = new Vector<String>();

   private boolean started = false;

   static ArrayList<ChatThread> clients = new ArrayList<ChatThread>();

   static ArrayList<ChatThread> waitting = new ArrayList<ChatThread>();

   Random randomclient = new Random();

   private ChangeThread changeThread;

   static int check = 0;

   static int id = 0;

   public ChatServer() {

	   jp.add(jl);

	   jp.add(jbStart);

	   add(jp,BorderLayout.NORTH);

	   add(jsp);

	   jp2.add(jonline);

	   add(jp2,BorderLayout.SOUTH);

	   jbStart.addActionListener(new ActionListener(){		

		public void actionPerformed(ActionEvent e) {

			startServer(e);		

			

		}		   

	   });

	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	   setBounds(150,150,400,400);

	   setVisible(true);

   }

   

   

   public void startServer(ActionEvent e) {

	   if(!started) {

		   try {

			 serverSocket = new ServerSocket(8000);

			listData.clear();

			listData.add(String.format("%tF %<tT", System.currentTimeMillis())+" >> server啓動成功，在8000監聽...");

		    list.setListData(listData);

		    started = true;

		    jbStart.setText("停止server");

		    changeThread = new ChangeThread(serverSocket);

		    changeThread.start();

		    new Thread(new Runnable(){					

				public void run() {

					while(! serverSocket.isClosed()) {

			    		try {

							Socket socket = serverSocket.accept(); //不斷接收client的請求

							ChatThread client = null;

						    client = new ChatThread(socket,ChatServer.this);

						    clients.add(client);

						    waitting.add(client);  //先將client放入等待序列

							client.start(); //啓動client的thread

							listData.add(String.format("%tF %<tT", System.currentTimeMillis()) + " >> " + socket.getInetAddress().getHostAddress() + "進入聊天室");

						    list.setListData(listData);

						    String numbers = Integer.toString(clients.size());

							jonline.setText("線上人數：" + numbers);

			    		} catch (IOException e) {						

							//e.printStackTrace();

			    			break;

						} 	    		

			    	}

				}		    	

		    }).start();			    

		   } catch (IOException e1) {		

			e1.printStackTrace();

		}

	   } else {

		   stopServer();

	   }

	   

   }

   

   public void sendMessage(String msg,int id) {

	   for(ChatThread client : clients) {

		   if(client.getNumber()==id){

		   client.sendMessage(msg);

		   }

	   }

   }

   

   public static void main(String[] args) {

	  new ChatServer();

  }

   public void changeClient(ChatThread client) {

	   int i = 0;

	   clients.remove(client);

	   while(i<clients.size()&&clients.get(i).getNumber()!=client.getNumber()){

		   i++;

	   }

	   clients.add(client);

	   waitting.add(clients.get(i));

	   waitting.add(client);

       

       

   }	

   

   public void removeClient(ChatThread client) {

	   int i = 0;

	   if(waitting.contains(client)){     //如果離開的client是在等待佇列裡，就把他從waitting中除掉。

		   waitting.remove(client);   

		   clients.remove(client);

		   check = 0;

		   listData.add(String.format("%tF %<tT", System.currentTimeMillis()) + " >> " + client.getName() + "退出聊天室");

		   list.setListData(listData);

		   String numbers = Integer.toString(clients.size());

			jonline.setText("線上人數：" + numbers);

	   }

	   else {

	   clients.remove(client);

	   listData.add(String.format("%tF %<tT", System.currentTimeMillis()) + " >> " + client.getName() + "退出聊天室");

	   list.setListData(listData);  

	   String numbers = Integer.toString(clients.size());

		jonline.setText("線上人數：" + numbers);

	   //找出離開的client所對應的另一方。

	   while(i<clients.size()&&clients.get(i).getNumber()!=client.getNumber()){

		   i++;

	   }

	    if(i<clients.size()){   //找到後，將他放入waitting佇列中

	    waitting.add(clients.get(i));

	    }                      

	   }

	   client.release();

   }

   

   public void stopServer() {

	   for(ChatThread client : clients) {

		   client.release();

	   }

	   clients.removeAll(clients);

	   waitting.removeAll(waitting);

	   if(serverSocket != null && ! serverSocket.isClosed()) {

		   try {

			serverSocket.close();

		} catch (IOException e) {			

			e.printStackTrace();

		}

	   }

	   listData.add(String.format("%tF %<tT", System.currentTimeMillis())+" >> server停止...");

	    list.setListData(listData);

	   jbStart.setText("啓動服務器");

	   jonline.setText("線上人數：0");

	   started = false;

   }

}

