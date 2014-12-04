package org.knime.knip.python.extensions;

import org.scijava.Priority;
import org.scijava.app.StatusService;
import org.scijava.app.event.StatusEvent;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 * In our use-case we really don't want to make use of the StatusService, so wee
 * keep the methods empty
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Service.class, priority = Priority.FIRST_PRIORITY)
public class FasterStatusService extends AbstractService implements
		StatusService {

	@Override
	public double getPriority() {
		return Priority.FIRST_PRIORITY;
	}

	@Override
	public void showProgress(int value, int maximum) {
		// Nothing to do
	}

	@Override
	public void showStatus(String message) {
		// Nothing to do
	}

	@Override
	public void showStatus(int progress, int maximum, String message) {
		// Nothing to do
	}

	@Override
	public void showStatus(int progress, int maximum, String message,
			boolean warn) {
		// Nothing to do
	}

	@Override
	public void warn(String message) {
		// Nothing to do
	}

	@Override
	public void clearStatus() {
		// Nothing to do
	}

	@Override
	public String getStatusMessage(String appName, StatusEvent statusEvent) {
		return "Nothing";
	}
}
