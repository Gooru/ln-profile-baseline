
package org.gooru.profilebaseline.infra.services.baselineprofileremover;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public class ProfileBaselineRemoverImpl implements ProfileBaselineRemover {

  private final DBI dbi;
  private ProfileBaselineRemoverDao dao;

  public ProfileBaselineRemoverImpl(DBI dbiForDefaultDS) {
    this.dbi = dbiForDefaultDS;
  }

  @Override
  public void remove(ProfileBaselineQueueModel model) {
    fetchDao().removeProfileBaseline(model.getClassId().toString(), model.getCourseId().toString(),
        model.getUserId().toString());
  }

  private ProfileBaselineRemoverDao fetchDao() {
    if (dao == null) {
      return dbi.onDemand(ProfileBaselineRemoverDao.class);
    }
    return dao;
  }
}
