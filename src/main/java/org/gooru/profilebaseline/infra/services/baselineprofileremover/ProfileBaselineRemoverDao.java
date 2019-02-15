
package org.gooru.profilebaseline.infra.services.baselineprofileremover;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public interface ProfileBaselineRemoverDao {

  @SqlUpdate("DELETE FROM learner_profile_baselined WHERE class_id = :classId AND course_id = :courseId AND user_id = :userId")
  void removeProfileBaseline(@Bind("classId") String classId, @Bind("courseId") String courseId, @Bind("userId") String userId);
}
