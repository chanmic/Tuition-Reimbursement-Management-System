package com.revature.services;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.Message;
import com.revature.beans.TuitionReimbursement;
import com.revature.beans.Upload;

public interface ReimbursementService {
	TuitionReimbursement getReimbursement(int id);
	boolean createReimbursement(TuitionReimbursement t);
	int nextId();
	int nextMessageId();
	List<Message> getMessages(String username); 
	boolean sendMessage(Message m);
	boolean removeMessage(int messageId);
	boolean updateReimbursement(TuitionReimbursement t);
	boolean addUpload(Upload upload);
	List<Upload> getUploads(Employee employee);
}
