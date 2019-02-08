
package org.gooru.profilebaseline.infra.services.route0remover;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author szgooru Created On 08-Feb-2019
 */
public interface Route0RemoverDao {

  @SqlUpdate("DELETE FROM user_route0_content WHERE class_id = :classId AND course_id = :courseId AND user_id = :userId")
  void removeRoute0Content(@Bind("classId") UUID classId, @Bind("courseId") UUID courseId, @Bind("userId") UUID userId);
}
