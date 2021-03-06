package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

/** A very simple test just to ensure that JPA is set up correctly. */
public class JpaSetupTest {

	@Test
	public void testStartup() throws Exception {

		System.out.println("SetupTest.setupTest");

		EntityManagerFactory entityMgrFactory = 
			Persistence.createEntityManagerFactory("hibernatesearchdemo");
		EntityManager entityManager = entityMgrFactory.createEntityManager();
		
		entityManager.close();
		entityMgrFactory.close();
		
		System.out.println("Completed OK");
	}
}
