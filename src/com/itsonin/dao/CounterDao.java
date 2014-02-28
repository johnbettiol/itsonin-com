package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.ConcurrentModificationException;

import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.itsonin.entity.Counter;

/**
 * @author nkislitsin
 *
 */
public class CounterDao {
		
	public CounterDao() {
		
	}

	public long next(String name) {
		Objectify trans = ofy().transaction();
		int triesLeft = 5;
		
		while (true) {
			try {
				Counter counter;
				try{
					if (!trans.getTxn().isActive())
						trans = ofy().transaction();
					counter = trans.load().type(Counter.class).id(name).safe();
					counter.setValue(counter.getValue() + 1L);
				}catch(NotFoundException e){
					counter = new Counter(name, 1L);
				}
					
				trans.save().entity(counter).now();
				trans.getTxn().commit();
				return counter.getValue();
			}catch (ConcurrentModificationException e) {
				if (triesLeft == 0) {
					throw e;
				}
				--triesLeft;
			} finally {
				if (trans.getTxn().isActive()) {
					trans.getTxn().rollback();
				}
			}
		}
	}
	
	public long getCount(String name) {
		try{
			Counter counter = ofy().load().type(Counter.class).id(name).safe();
			if(counter == null)
				return 0;
			else
				return counter.getValue();
		}catch(NotFoundException e){
			return 0;
		}
	}
	
	public void setCount(String name, long value) {
		Counter counter;
		try{
			counter = ofy().load().type(Counter.class).id(name).safe();
			counter.setValue(value);
		}catch(NotFoundException e){
			counter = new Counter(name, value);
		}
		ofy().save().entity(counter).now();
	}

}
