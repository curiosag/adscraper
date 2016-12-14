package org.cg.util.parallel; 

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.cg.common.threading.Parallel.TaskHandler;
import org.gwtTests.base.Check;
import org.gwtTests.base.Const;
import org.gwtTests.base.Log;

import com.google.common.base.Optional;

public class TaskHandlerExt<V> {

	private TaskHandler<V> handler;

	private TaskHandlerExt(TaskHandler<V> handler) {
		this.handler = handler;
	}

	public static <V> TaskHandlerExt<V> from(TaskHandler<V> handler) {
		Check.notNull(handler);
		return new TaskHandlerExt<V>(handler);
	}

	public Optional<Collection<V>> values() {
		try {
			return Optional.of(handler.values());
		} catch (InterruptedException | ExecutionException e) {
			Log.logException(e, Const.ADD_STACK_TRACE);
			return Optional.absent();
		}
	}

	public void awaitCompletion() {
		values();
	}

}
