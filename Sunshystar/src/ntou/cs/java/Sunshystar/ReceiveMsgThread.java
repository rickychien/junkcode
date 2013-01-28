package ntou.cs.java.Sunshystar;

import java.io.IOException;

/*
 * ReceiveMsgThread 負責不斷的接收Server傳來的訊息
 * 會判斷Server傳過來的是一般訊息還是特殊訊息，分別做不同的處理
 * 
 * @author Ricky
 */
public class ReceiveMsgThread implements Runnable {
	String msg = null;
	String uid = null;
	int pairUID = 0;
	int firstMessage = 0;

	public void run() {
		//開啟接收訊息
		try {
			while ((msg = ChatRoom.receiveMsgReader.readLine()) != null) {
				//如果配對成功，先互相交換UID
				if(msg.matches("uid=+\\d")) {
					pairUID = Integer.parseInt(msg.substring(4));
					
					//取得對方UID，不過會同時收到自己UID，所以需要先過濾
					if (pairUID != Sunshystar.getMeUID()) {
						Sunshystar.setYouUID(pairUID);
					}
					
					//更新對方個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
					
					//不需要把uid msg印出來
					continue;
				}
				else if(msg.equals("有人要跟你聊囉！！")) {
					//如果配對成功就互相交換UID
					ChatRoom.sendMsgWriter.println("uid=" + Sunshystar.getMeUID());
				}
				else if(msg.equals("等待中")) {
					//設定預設Sunshystar基本資訊
					Sunshystar.setYouUID(0);
					
					//更新對方個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
				}
				else if(msg.equals("uid=" + Sunshystar.getMeUID() + "個人資料更新了！")) {
					msg = "個人資料更新成功！";
					//更新個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
				}
				else if(msg.equals("uid=" + Sunshystar.getYouUID() + "個人資料更新了！")) {
					msg = "對方更新了個人資料！";
					//更新對方個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
				}
				else if(msg.equals("uid=" + Sunshystar.getMeUID() + "個人照片更新了！")) {
					msg = "您的個人照片更換了！";
					//更新個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
				}
				else if(msg.equals("uid=" + Sunshystar.getYouUID() + "個人照片更新了！")) {
					msg = "對方更換了個人照片囉！";
					//更新對方個人資本資料與大頭照
					Sunshystar.chatRoomGUI.setChatRoomGUIInfo();
				}
				
				//印出收到的訊息至ChatRoomGUI上
				ChatRoom.listData.add(msg);
				if( firstMessage == 0 ){
					ChatRoomGUI.messageBox.append(msg);
					firstMessage = 1;
				}
				else {
					ChatRoomGUI.messageBox.append("\n" + msg);
				}
				
				ChatRoomGUI.messageBox.setCaretPosition(ChatRoomGUI.messageBox.getText().length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
