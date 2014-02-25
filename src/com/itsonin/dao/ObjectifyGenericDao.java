package com.itsonin.dao;

import static com.itsonin.dao.OfyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

/**
 * @author nkislitsin
 *
 */
public class ObjectifyGenericDao<T>
{
	
	protected Class<T> clazz;

	protected ObjectifyGenericDao(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public int count(){
		return ofy().load().type(clazz).count();
	}
	
	public int countByProperty(String propName, Object propValue){
		return ofy().load().type(clazz).filter(propName, propValue).count();
	}
	
	public Key<T> save(T entity){
		return ofy().save().entity(entity).now();
	}
	
	public T saveAndGet(T entity){
		Key<T> key = ofy().save().entity(entity).now();
		return ofy().load().key(key).now();
	}
	
	public void saveAll(Collection<T> entities){
		ofy().save().entities(entities).now();
	}
	
	public void delete(T entity){
		ofy().delete().entity(entity);
	}
	
	public void delete(List<T> entities){
		ofy().delete().entities(entities);
	}
	
	public void delete(Key<T> key){
		ofy().delete().key(key);
	}
	
	public void delete(Long id){
		ofy().delete().type(clazz).id(id);
	}
	
	public void delete(String id){
		ofy().delete().type(clazz).id(id);
	}
	
	public void deleteAll(List<Key<T>> keys){
		ofy().delete().keys(keys);
	}
	
	public void deleteAll(Iterable <Key<T>> keys){
		ofy().delete().keys(keys);
	}

	public T get(Long id){
		//ofy().clear();
		return ofy().load().type(clazz).id(id).now();
	}
	
	public T getNoCache(Long id){
		return ofy().cache(false).load().type(clazz).id(id).now();
	}
	
	public T safeGet(Long id){
		return ofy().load().type(clazz).id(id).safe();
	}
	
	public T get(String id){
		return ofy().load().type(clazz).id(id).now();
	}
	
	public T safeGet(String id){
		return ofy().load().type(clazz).id(id).safe();
	}
	
	public T get(Key<T> key){
		return ofy().load().key(key).now();
	}
	
	public Collection<T> getAll(List<Key<T>> keys){
		return ofy().cache(true).load().keys(keys).values();
	}

	public T getByProperty(String propName, Object propValue){
		return ofy().load().type(clazz).filter(propName, propValue).first().now();
	}
	
	public List<T> listByIds(Collection<Long> ids){
		Map<Long, T> map = ofy().load().type(clazz).ids(ids);
		if(map != null)
			return new ArrayList<T>(map.values());
		else 
			return null;
	}
	
	public Map<Long, T> mapByIds(Collection<Long> ids){
		return ofy().load().type(clazz).ids(ids);
	}
	
	public List<T> list(){
		return ofy().cache(true).load().type(clazz).chunk(1000).list();
	}
	
	public List<T> list(String sortOrder){
		return ofy().load().type(clazz).order(sortOrder).list();
	}
	
	public List<T> listByProperty(String propName, Object propValue){
		return ofy().load().type(clazz).filter(propName, propValue).list();
	}
	
	public Query<T> query(int limit, String sortOrder){
		return ofy().load().type(clazz).order(sortOrder).limit(limit);
	}
	
	public Query<T> query(){
		return ofy().load().type(clazz);
	}

}

