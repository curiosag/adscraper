package app.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cg.common.check.Check;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class Repos implements ApplicationContextAware {

	ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	private ApplicationContext getContext() {
		Check.notNull(context);
		return context;
	}

	public List<RepositoryItem> getItems() {
		// give no access to instances without prior knowledge of domain classes
		// return correct number of repos, though
		
		List<RepositoryItem> result = new ArrayList<RepositoryItem>();
		for (Repository item : get()) {
			if (item instanceof CrudRepository) {
				CrudRepository crud = (CrudRepository) item;
				RepositoryItem repoInfo = getRepoItem(crud);
				// TODO multiple occurences of single repositories?
				if (!findItem(repoInfo, result))
					result.add(getRepoItem(crud));
			}
		}
		return result;
	}

	private boolean findItem(RepositoryItem item, List<RepositoryItem> repos) {
		for (RepositoryItem r : repos)
			if (r._class.equals(item._class))
				return true;
		return false;
	}

	private List<Repository> get() {
		ArrayList result = new ArrayList();
		for (Repository r : getContext().getBeansOfType(Repository.class).values())
			result.add(r);
		return result;
	}

	public int count() {
		return get().size();
	}

	private RepositoryItem getRepoItem(CrudRepository r) {
		Class _class = Object.class;
		long size = 0;
		size = r.count();
		Iterator items = r.findAll().iterator();
		if (items.hasNext()) {
			Object item = items.next();
			_class = item.getClass();
		}

		return new RepositoryItem(_class, r, size);
	}
}
