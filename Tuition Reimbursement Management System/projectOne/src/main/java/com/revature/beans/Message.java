package com.revature.beans;

public class Message {
	private int messageId;
	private int reimbursementId;
	private String recipient;
	private String sender;
	private String reason;
	
	public Message(int messageId, int reimbursementId, String recipient, String sender, String reason) {
		super();
		this.messageId = messageId;
		this.reimbursementId = reimbursementId;
		this.recipient = recipient;
		this.sender = sender;
		this.reason = reason;
	}

	public int getReimbursementId() {
		return reimbursementId;
	}

	public void setReimbursementId(int reimbursementId) {
		this.reimbursementId = reimbursementId;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	
	
}
