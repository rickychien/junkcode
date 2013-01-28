import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

/*
 * 負責監聽clients(存放目前線上client的佇列)與waitting(client的等待佇列)
 * 進行相對應的client端狀態轉換 (聊天中、等待中)
 * 
 * @author Arvin
 */
public class ChangeThread extends Thread{
    private ArrayList<ChatThread> waitting;
    private int id;
    private ServerSocket server;
    private Random randomclient = new Random();
	public ChangeThread(ServerSocket server){
	  this.server = server;
	  this.waitting = ChatServer.waitting;
	  this.id = ChatServer.id;
  }
  public void run(){
	  
	 while(!server.isClosed()){
		 
	  if(waitting.size()==1 && ChatServer.check == 0){  //若等待序列中只有客戶端一個人
	    	waitting.get(0).sendMessage("等待中");
	    	waitting.get(0).setNumber(id++); //設定此客戶端的序號
	    	ChatServer.check = 1;
	    }
	    else if(waitting.size()>1){   //若此客戶進入server時，等待序列中已有人
	    	ChatServer.check = 0;
	    	int rand = randomclient.nextInt(waitting.size()); //從等待序列中把等待的人抓出
	    	int rand2 = randomclient.nextInt(waitting.size());
	    	while(rand==rand2){
	    		rand = randomclient.nextInt(waitting.size()); 
		    	rand2 = randomclient.nextInt(waitting.size());
	    	}
          waitting.get(rand).setNumber(id);  //將此等待的人與剛登入的人進行配合
          waitting.get(rand).sendMessage("有人要跟你聊囉！！");
          waitting.get(rand2).setNumber(id);  //將此等待的人與剛登入的人進行配合
          waitting.get(rand2).sendMessage("有人要跟你聊囉！！");
          if (rand > rand2){
          waitting.remove(rand);  //將原等待人移除
          waitting.remove(rand2);//將客戶端從等待列中移除
          }
          else {
          waitting.remove(rand2);  //將原等待人移除
          waitting.remove(rand);//將客戶端從等待列中移除	  
          }
          id++;  //客戶序號繼續增加
	    }
	 }
  }
}
