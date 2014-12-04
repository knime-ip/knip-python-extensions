package org.knime.knip.python.extensions;

import io.scif.io.IRandomAccess;
import io.scif.io.VirtualHandle;
import io.scif.services.DefaultLocationService;
import io.scif.services.LocationService;

import java.io.IOException;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * We only have streams in this use-case. Therefore we restrict the getHandle
 * method to VirtualHandles
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Service.class, priority = Priority.FIRST_PRIORITY)
public class FasterLocationService extends DefaultLocationService implements
		LocationService {

	@Override
	public double getPriority() {
		return Priority.FIRST_PRIORITY;
	}

	@Override
	public IRandomAccess getHandle(final String id, final boolean writable,
			final boolean allowArchiveHandles) throws IOException {
		final IRandomAccess handle = getMappedFile(id);

		if (handle == null) {
			return new VirtualHandle(getMappedId(id));
		} else {
			return handle;
		}

	}
}
