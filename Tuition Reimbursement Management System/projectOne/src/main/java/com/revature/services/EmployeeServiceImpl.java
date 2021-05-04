package com.revature.services;

import com.revature.beans.Employee;
import com.revature.data.EmployeeDao;
import com.revature.data.EmployeeDaoImpl;
import com.revature.factory.BeanFactory;
import com.revature.factory.Log;

@Log
public class EmployeeServiceImpl implements EmployeeService {
	private EmployeeDao ed = (EmployeeDao) BeanFactory.getFactory().get(EmployeeDao.class, EmployeeDaoImpl.class);

	@Override
	public boolean addEmployee(Employee employee) {
		if(ed.getEmployee(employee.getUsername()) == null) { // If employee cannot be found
			try {
				ed.addEmployee(employee);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
		

	}

	@Override
	public Employee getEmployee(String username) {
		try {
			return ed.getEmployee(username);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateEmployee(Employee employee) {
		try {
			ed.updateEmployee(employee);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
