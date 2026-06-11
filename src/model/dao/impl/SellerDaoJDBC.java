package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	private Connection conn;
	public SellerDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Seller findByID(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*, department.Name as DepName "+
					"from seller inner join department "+
					"on Seller.DepartmentId = department.Id "+
					"where seller.ID = ?");
			st.setInt(1, id);
			rs = st.executeQuery(); // agora já tem o resultado da consulta no formato de tabela, quero passar para o formato de objetos
			
			/*o objeto rs inicia na posição 0 e a primeira posição que tem alguma coisa é a posição 1
			 * então se o rs.next() não tem nada, é pq o resultado da consulta foi nulo*/
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller (rs, dep);
				return seller;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}



	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		Department dep = new Department ();
		dep.setId(rs.getInt("DepartmentId")); //nome da coluna que eu quero
		dep.setName(rs.getString("DepName")); //nome da coluna que eu quero
		return dep;
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	
	
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*, department.Name as DepName "+
					"from seller inner join department "+
					"on Seller.DepartmentId = department.Id "+
					"Order by Name ");
			rs = st.executeQuery(); 
			List <Seller> list = new ArrayList<>();
			    //jeito do professor
			 Map <Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
			
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*, department.Name as DepName "+
					"from seller inner join department "+
					"on Seller.DepartmentId = department.Id "+
					"where departmentId = ? " +
					"Order by Name ");
			st.setInt(1, department.getId());
			rs = st.executeQuery(); 
			List <Seller> list = new ArrayList<>();
			/*    //jeito do professor
			 Map <Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
			*/
			
			//meu jeito
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				do {
					Seller obj = instantiateSeller(rs, dep);
					list.add(obj);
				}while (rs.next());
			}

			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
