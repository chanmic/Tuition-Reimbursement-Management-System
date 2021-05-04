package com.revature.services;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.Message;
import com.revature.beans.TuitionReimbursement;
import com.revature.beans.Upload;
import com.revature.data.ReimbursementDao;
import com.revature.data.ReimbursementDaoImpl;
import com.revature.factory.BeanFactory;
import com.revature.factory.Log;

@Log
public class ReimbursementServiceImpl implements ReimbursementService {
	ReimbursementDao rd = (ReimbursementDao) BeanFactory.getFactory().get(ReimbursementDao.class, ReimbursementDaoImpl.class);

	@Override
	public boolean createReimbursement(TuitionReimbursement t) {
		try {
			rd.addReimbursement(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int nextId() {
		try {
			return rd.nextId();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int nextMessageId() {
		try {
			return rd.nextMessageId();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public TuitionReimbursement getReimbursement(int id) {
		try {
			return rd.getReimbursementById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Message> getMessages(String username) {
		try {
			return rd.getMessages(username);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean sendMessage(Message m) {
		try {
			rd.addMessage(m);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeMessage(int messageId) {
		try {
			rd.removeMessage(messageId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateReimbursement(TuitionReimbursement t) {
		try {
			rd.updateReimbursement(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<Upload> getUploads(Employee employee) {
		try {
			return rd.getUploads(employee);
		} catch (Exception e) {
			
			return null;
		}
	}

	@Override
	public boolean addUpload(Upload upload) {
			rd.addUpload(upload);
			return true;

	}



}
