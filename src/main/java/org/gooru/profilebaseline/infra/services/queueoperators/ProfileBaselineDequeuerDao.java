package org.gooru.profilebaseline.infra.services.queueoperators;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish.
 */

interface ProfileBaselineDequeuerDao {

  @SqlUpdate("delete from learner_profile_baselined where id = :modelId")
  void dequeueRecord(@Bind("modelId") Long id);

}
