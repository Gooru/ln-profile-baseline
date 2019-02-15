
package org.gooru.profilebaseline.infra.services.baselineprofileremover;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public interface ProfileBaselineRemover {

  void remove(ProfileBaselineQueueModel model);

  static ProfileBaselineRemover build(DBI dbi) {
    return new ProfileBaselineRemoverImpl(dbi);
  }

  static ProfileBaselineRemover build() {
    return new ProfileBaselineRemoverImpl(DBICreator.getDbiForDefaultDS());
  }

}
