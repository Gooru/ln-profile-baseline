
package org.gooru.profilebaseline.infra.services.route0remover;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public interface Route0Remover {

  void remove(ProfileBaselineQueueModel model);

  static Route0Remover build(DBI dbi) {
    return new Route0RemoverImpl(dbi);
  }

  static Route0Remover build() {
    return new Route0RemoverImpl(DBICreator.getDbiForDefaultDS());
  }
}
