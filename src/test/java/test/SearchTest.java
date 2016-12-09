package test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.MusicRecording;
import model.Track;

public class SearchTest {
	
	protected static EntityManagerFactory emf;
	protected static EntityManager em;
	protected static FullTextEntityManager fullTextEntityManager;

	@BeforeClass
	public static void init() throws InterruptedException {
		emf = Persistence.createEntityManagerFactory("hibernatesearchdemo");
		em = emf.createEntityManager();
		fullTextEntityManager = Search.getFullTextEntityManager(em);
		fullTextEntityManager.createIndexer(MusicRecording.class).startAndWait();
		System.out.println("Indexing done");
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		// Create a dummy Music Recording
		MusicRecording mr0 = new MusicRecording();
		mr0.setArtist("Me");
		mr0.setTitle("All About Me");
		
		// Create a MR that SHOULD be found
		Track t1 = new Track("Jivin' on a SUNny day");
		Track t2 = new Track("Consulting the Oracle");
		MusicRecording mr = new MusicRecording();
		mr.setTitle("Java Greatest Hits");
		mr.setArtist("Walkalong Javvity");
		mr.setTracks(Arrays.asList((new Track[]{t1,t2})));
		
		// Create a MR that SHOULD NOT be found
		Track t21 = new Track("Jivin' on a SUNny day");
		Track t22 = new Track("Consulting the Oracle");
		MusicRecording mr2 = new MusicRecording();
		mr2.setTitle("My Greatest Nits");
		mr2.setArtist("Me, Myself and HavaKava"); // substring, should not match
		mr2.setTracks(Arrays.asList((new Track[]{t21,t22})));

		em.getTransaction().begin();

		// Create one entity that we can search on
		em.persist(mr0);
		em.persist(mr);
		em.persist(mr2);
		em.getTransaction().commit();
		
		// Now do the search.
		// TODO: Use better Analyzer, change this to just "Java"
		final String queryString = "Java";
		
		// Create query using Hibernate Search DSL.
		QueryBuilder qb = fullTextEntityManager.getSearchFactory()
			.buildQueryBuilder().forEntity(MusicRecording.class).get();
		// Both JPA and Lucene have Query class, hence fully qualified name;
		org.apache.lucene.search.Query luceneQuery = qb
			.keyword()
			.onFields("title", "artist")
			.matching(queryString)
			.createQuery();

		// wrap Lucene Query in JPA Query
		Query jpaQuery = 
			fullTextEntityManager.createFullTextQuery(luceneQuery, MusicRecording.class);

		System.out.println("Executing search");
		em.getTransaction().begin();
		List<MusicRecording> results = jpaQuery.getResultList();
		em.getTransaction().commit();
		em.close();
		
		System.out.println("Results:");
		for (MusicRecording mrec : results) {
			System.out.println(mrec);
		}
		assertEquals("Didn't find searched-for object", 1, results.size());
		MusicRecording found = results.get(0);
		System.out.println(found.getId() + "--" + found.getTitle());
	}
}
