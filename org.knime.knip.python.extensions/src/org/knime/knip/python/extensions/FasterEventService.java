package org.knime.knip.python.extensions;

import java.util.ArrayList;
import java.util.List;

import org.scijava.Priority;
import org.scijava.event.DefaultEventService;
import org.scijava.event.EventService;
import org.scijava.event.EventSubscriber;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * In our use-case we really don't want to make use of the EventService, so we
 * enforce that nobody can ever subscribe to it.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Service.class, priority = Priority.FIRST_PRIORITY)
public class FasterEventService extends DefaultEventService implements
		EventService {

	final ArrayList<EventSubscriber<?>> EMPTYLIST = new ArrayList<EventSubscriber<?>>();

	@Override
	public List<EventSubscriber<?>> subscribe(Object o) {
		return EMPTYLIST;
	}

	@Override
	public double getPriority() {
		return Priority.FIRST_PRIORITY;
	}

}
