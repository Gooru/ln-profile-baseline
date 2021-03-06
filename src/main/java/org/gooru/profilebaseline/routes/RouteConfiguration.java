package org.gooru.profilebaseline.routes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ashish.
 */
public class RouteConfiguration implements Iterable<RouteConfigurator> {

  private final Iterator<RouteConfigurator> internalIterator;

  public RouteConfiguration() {
    List<RouteConfigurator> configurators = new ArrayList<>(32);
    // First the global handler to enable to body reading etc
    configurators.add(new RouteGlobalConfigurator());

    // For rest of handlers, Auth should always be first one
    configurators.add(new RouteInternalConfigurator());
    configurators.add(new RouteFailureConfigurator());
    configurators.add(new RouteProfileBaselineConfigurator());
    internalIterator = configurators.iterator();
  }

  @Override
  public Iterator<RouteConfigurator> iterator() {
    return new Iterator<RouteConfigurator>() {

      @Override
      public boolean hasNext() {
        return internalIterator.hasNext();
      }

      @Override
      public RouteConfigurator next() {
        return internalIterator.next();
      }

    };
  }

}
