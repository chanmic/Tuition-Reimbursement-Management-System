package com.revature.data;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.Message;
import com.revature.beans.TuitionReimbursement;
import com.revature.beans.Upload;

public interface ReimbursementDao {
	TuitionReimbursement getReimbursementById(int id);
	void addReimbursement(TuitionReimbursement t);
	void updateReimbursement(TuitionReimbursement t);
	int nextId();
	int nextMessageId();
	List<Message> getMessages(String username);
	void addMessage(Message m);
	void removeMessage(int messageId);
	List<Upload> getUploads(Employee employee);
	void addUpload(Upload upload);
}
