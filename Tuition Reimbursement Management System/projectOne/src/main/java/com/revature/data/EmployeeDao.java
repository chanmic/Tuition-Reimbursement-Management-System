package com.revature.data;

import com.revature.beans.Employee;

public interface EmployeeDao {
	Employee getEmployee(String username);
	void addEmployee(Employee e);
	void updateEmployee(Employee e);
}
