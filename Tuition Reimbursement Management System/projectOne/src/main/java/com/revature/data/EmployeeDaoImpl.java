package com.revature.data;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.factory.Log;
import com.revature.utils.CassandraUtil;

@Log
public class EmployeeDaoImpl implements EmployeeDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	
	@Override
	public Employee getEmployee(String username) {
		Employee e = null;
		String query = "Select username, password, role, formid, availablereimbursement from employee where username = ?;";
		BoundStatement bound = session.prepare(query).bind(username);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			e = new Employee(data.getString("username"),
					data.getString("password"),
					data.getInt("role"),
					data.getList("formid", Integer.class),
					data.getDouble("availablereimbursement")
					);
		}
		return e;
		
	}

	@Override
	public void addEmployee(Employee e) {
		String query = "Insert into employee (username, password, role, formid, availablereimbursement) values (?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(e.getUsername(), 
				e.getPassword(), 
				e.getRole(), 
				e.getFormId(),
				e.getAvailableReimbursement()
				);
		session.execute(bound);
		
	}
	
	@Override
	public void updateEmployee(Employee e) {
		String query = "Update employee set formid = ?, availablereimbursement = ? where username = ?";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(
				e.getFormId(),
				e.getAvailableReimbursement(),
				e.getUsername()
				);
		session.execute(bound);
		
	}

}
