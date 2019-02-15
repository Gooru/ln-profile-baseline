
package org.gooru.profilebaseline.infra.services.route0remover;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public class Route0RemoverImpl implements Route0Remover {

  private final DBI dbi;
  private Route0RemoverDao dao;

  public Route0RemoverImpl(DBI dbiForDefaultDS) {
    this.dbi = dbiForDefaultDS;
  }

  @Override
  public void remove(ProfileBaselineQueueModel model) {
    fetchDao().removeRoute0Content(model.getClassId(), model.getCourseId(), model.getUserId());
  }


  private Route0RemoverDao fetchDao() {
    if (dao == null) {
      return dbi.onDemand(Route0RemoverDao.class);
    }
    return dao;
  }
}
