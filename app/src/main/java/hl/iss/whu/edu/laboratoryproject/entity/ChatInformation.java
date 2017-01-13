package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * 一个消息的JavaBean
 * 
 * @author way
 * 
 */
public class ChatInformation {
	private String name;//消息来自
	private String date;//消息日期
	private String message;//消息内容
	private boolean isComMeg = true;// 是否为收到的消息
	private byte[] image;

	public ChatInformation(String name, String date, String message, boolean isComMeg, byte[] image) {
		this.name = name;
		this.date = date;
		this.message = message;
		this.isComMeg = isComMeg;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatInformation() {
	}



	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean comMeg) {
		isComMeg = comMeg;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
