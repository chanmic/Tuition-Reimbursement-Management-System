package com.revature.data;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.beans.Event;
import com.revature.beans.GradingFormat;
import com.revature.beans.Message;
import com.revature.beans.TuitionReimbursement;
import com.revature.beans.Upload;
import com.revature.factory.Log;
import com.revature.utils.CassandraUtil;

@Log
public class ReimbursementDaoImpl implements ReimbursementDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();

	@Override
	public TuitionReimbursement getReimbursementById(int id) {
		String query = "Select * from reimbursement where id=?";
		BoundStatement bound = session.prepare(query).bind(id);
		ResultSet results = session.execute(bound);
		Row row = results.one();

		TuitionReimbursement t = new TuitionReimbursement(id,
				row.getString("employee"),
				row.getString("directsupervisor"),
				row.getString("departmenthead"),
				row.getString("benco"),
				row.getString("date"),
				row.getString("time"),
				row.getString("location"),
				row.getString("description"),
				row.getDouble("cost"),
				new GradingFormat(row.getDouble("gradingpercentage"), row.getBoolean("presentationrequired")),
				Event.valueOf(row.getString("event")),
				row.getString("justification"),
				row.getInt("approval"),
				row.getBoolean("urgent"),
				row.getDouble("pendingreimbursement"),
				row.getBoolean("exceedsfunds"),
				row.getList("attachments", String.class)
				);
		return t;
	}

	@Override
	public void addReimbursement(TuitionReimbursement t) {
		String query = "Insert into reimbursement (id, employee, directsupervisor, departmenthead, benco, date, time, location, description, cost, "
				+ "gradingpercentage, presentationrequired, event, justification, urgent, approval, pendingreimbursement, exceedsfunds, attachments) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(t.getId(),
				t.getEmployee(),
				t.getDirSu(),
				t.getDepHe(),
				t.getBenCo(),
				t.getDate(),
				t.getTime(),
				t.getLocation(),
				t.getDescription(),
				t.getCost(),
				t.getGradingFormat().getPassingGrade(),
				t.getGradingFormat().isPresentationRequired(),
				t.getEvent().toString(),
				t.getJustification(),
				t.isUrgent(),
				t.getApprovalLevel(),
				t.getPendingReimbursement(),
				t.isExceedsFunds(),
				t.getAttachments()
				);
		session.execute(bound);
		
	}

	@Override
	public void updateReimbursement(TuitionReimbursement t) {
		String query = "Update reimbursement set departmenthead = ?, benco = ?, date = ?, time = ?, location = ?, description = ?, cost = ?, "
				+ "gradingpercentage = ?, presentationrequired = ?, event = ?, justification = ?, urgent = ?, approval = ?, "
				+ "pendingreimbursement = ?, exceedsfunds = ?, attachments = ? where id = ? ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(
				t.getDepHe(),
				t.getBenCo(),
				t.getDate(),
				t.getTime(),
				t.getLocation(),
				t.getDescription(),
				t.getCost(),
				t.getGradingFormat().getPassingGrade(),
				t.getGradingFormat().isPresentationRequired(),
				t.getEvent().toString(),
				t.getJustification(),
				t.isUrgent(),
				t.getApprovalLevel(),
				t.getPendingReimbursement(),
				t.isExceedsFunds(),
				t.getAttachments(),
				t.getId()
				);
		session.execute(bound);
	}

	@Override
	public int nextId() {
		String query = "select * from reimbursement";
		ResultSet results = session.execute(query);
		return results.all().size() + 1;
	}

	@Override
	public int nextMessageId() {
		String query = "select * from message";
		ResultSet results = session.execute(query);
		return results.all().size() + 1;
	}

	@Override
	public List<Message> getMessages(String username) {
		List<Message> messages = new ArrayList<Message>();
		
		String query = "select messageid, reimbursementid, recipient, sender, reason from message where recipient = ?";
		BoundStatement bound = session.prepare(query).bind(username);
		ResultSet results = session.execute(bound);
		
		results.forEach(data -> {
			Message m = new Message(data.getInt("messageid"),
					data.getInt("reimbursementid"),
					data.getString("recipient"),
					data.getString("sender"),
					data.getString("reason")
					);
			messages.add(m);
		});
		
		return messages;

	}

	@Override
	public void addMessage(Message m) {
		String query = "Insert into message (messageid, reimbursementid, recipient, sender, reason) values (?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(m.getMessageId(),
				m.getReimbursementId(),
				m.getRecipient(),
				m.getSender(),
				m.getReason()
				);
		session.execute(bound);		
	}

	@Override
	public void removeMessage(int messageId) {
		String query = "delete from message where messageid = ? ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(messageId);
		session.execute(bound);		
		
	}
	
	@Override
	public List<Upload> getUploads(Employee employee) {
		List<Upload> uploads = new ArrayList<>();
		String query = "select * from upload where employeeId=?";
		BoundStatement bound = session.prepare(query).bind(employee.getUsername());
		ResultSet results = session.execute(bound);
		
		for(Row row : results){
			Upload upload = new Upload();
			upload.setEmployeeID(row.getString("employeeId"));
			upload.setFileURL(row.getString("fileURL"));
			upload.setS3Key(row.getString("s3Key"));

			uploads.add(upload);
		}
		
		return uploads;
	}

	@Override
	public void addUpload(Upload upload) {
		String query = "insert into upload (employeeId, fileURL, s3Key) "
				+ "values (?, ?, ?)";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(upload.getEmployeeID(), upload.getFileURL(), upload.getS3Key());
		session.execute(bound);
	}


}
