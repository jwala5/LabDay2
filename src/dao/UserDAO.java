package dao;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import utility.HibernateConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;

import model.User;
import utility.ConnectionManager;

public class UserDAO implements UserDaoInterface {
		
	private SessionFactory sessionFactory = HibernateConnectionManager.getSessionFactory();
	
	public int signUp(User user)  {
		Session session = this.sessionFactory.openSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		if(session.save(user)!=null)
		{
			transaction.commit();
			session.close();
			return 1;
		}
		else {
			return 0;
		}
		 
	}
	
	public boolean loginUser(User user)  {
	Session session= this.sessionFactory.openSession();
	Transaction tx = null;
	try {
		tx = (Transaction) session.getTransaction();
		((EntityTransaction) tx).begin();
		@SuppressWarnings("rawtypes")
		Query query = (Query) session.createQuery("from USERS where email='"+user.getEmail()+"'"+"and password='"+user.getPassword()+"'");
		user = (User)query.uniqueResult();
		tx.commit();
	}catch(Exception e)
	{
		if(tx!=null) {
			try{
				tx.rollback();
			}catch(IllegalStateException e1) {
				e1.printStackTrace();		
			}catch(SystemException e1)
			{
				e1.printStackTrace();
			}
			}
		e.printStackTrace();
	}
	finally {
		session.close();
	}
	return true;
	}

}