package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	public static void main (String args []) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("==== Test 1: Seller findById ======");
		Seller seller = sellerDao.findByID(3);
		System.out.println(seller);
		System.out.println("\n==== Test 2: Seller findById ======");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n==== Test 3: Seller findById ======");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n==== Test 4: Insert Seller ======");
		Seller newSeller = new Seller (null, "greg", "greg@gmail.com", new Date(), 4000.00, department);
		sellerDao.insert(newSeller);
		System.out.println("inserted! New id = " + newSeller.getId());
		
		System.out.println("\n==== Test 5: update Seller ======");
		Seller sellerToUpdate = sellerDao.findByID(1);
		sellerToUpdate.setName("arthurr");
		sellerDao.update(sellerToUpdate);
		System.out.println("Updated!\n" + sellerDao.findByID(1));
		
	}

}
