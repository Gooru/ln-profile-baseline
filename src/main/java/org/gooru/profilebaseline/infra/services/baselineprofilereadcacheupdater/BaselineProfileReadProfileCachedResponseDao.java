package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import java.util.List;
import org.gooru.profilebaseline.infra.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface BaselineProfileReadProfileCachedResponseDao {

  /*
   * NOTE: In the interest of performance, we are going to have specific sort orders. This should be
   * ASC for domains, but DESC for competencies in specified domains.
   * If this order is changed, make sure that downstream code also changes, as it assumes this order
   * to be present so that it can do minimal passes through list to generate output.
   */
  @Mapper(CachedResponseModelMapper.class)
  @SqlQuery(
      "SELECT cm.tx_domain_code, txd.tx_domain_name, txd.tx_domain_seq, cm.tx_comp_code, cm.tx_comp_name, "
          + " cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq, "
          + " case when cm.tx_comp_code = any(:competencies) then 4 else 0  END as status "
          + " FROM domain_competency_matrix cm INNER JOIN tx_domains txd ON cm.tx_subject_code = txd.tx_subject_code "
          + " and cm.tx_domain_code = txd.tx_domain_code "
          + " and cm.tx_subject_code = :subjectCode ORDER BY txd.tx_domain_seq ASC, cm.tx_comp_seq DESC ")
  List<CachedResponseModel> mapBaselinedProfileToDomainCompetencyMatrix(
      @Bind("competencies") PGArray<String> competencies, @Bind("subjectCode") String subjectCode);


}
