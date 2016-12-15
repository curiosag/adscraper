package app.storage;

import org.springframework.data.repository.CrudRepository;


@SuppressWarnings("rawtypes")
public class RepositoryItem {

	public final Class _class;
	public final CrudRepository repo;
	public final long size;
	
	public RepositoryItem(Class _class, CrudRepository repo, long size) {
		this._class = _class;
		this.repo = repo;
		this.size = size;
	}
}
