package org.szcloud.framework.unit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkflowSyncServiceImpl implements WorkflowSyncService {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkflowSyncServiceImpl.class);
	// @Resource
	// private SyncPartyProcessor syncPartyProcessor;

	@Override
	public void saveGroup(Long refId, String name, Long groupType) {
		// Integer count = syncPartyProcessor.getEntityCount(refId, groupType);
		// if(count == 0){
		// syncPartyProcessor.insertEntity(groupType, name, refId);
		// }
		// else{
		// syncPartyProcessor.updateEntity(name, refId, groupType);
		// }
		//
		// logger.debug(String.format("Insert/Save a group with refId =%1s and
		// groupType=%2s",refId, groupType));

	}

	@Override
	public void removeGroup(Long refId, Long groupType) {
		// syncPartyProcessor.removeEntity(refId, groupType);
		// logger.debug(String.format("Remove a group with refId =%1s and
		// groupType=%2s",refId, groupType));

	}

	@Override
	public void createRelation(Long parentRefId, Long parentGroupType, Long childRefId, Long childGroupType,
			Long relationType, Boolean isManager) {
		int admin = 0;
		if (isManager != null && isManager) {
			admin = 1;
		}
		// syncPartyProcessor.createRelation(parentGroupType, childGroupType,
		// relationType, parentRefId, childRefId,
		// 1, "1", 0, 0, admin);

		logger.debug(String.format(
				"Create relation with parent refId = %1s," + "parent type = %2s, " + "child refId = %3s, "
						+ "child type = %4s, " + "relation type = %5s" + "relation type = %6s",
				parentRefId, parentGroupType, childRefId, childGroupType, relationType, isManager));

	}

	@Override
	public void removeRelation(Long parentRefId, Long parentGroupType, Long childRefId, Long childGroupType,
			Long relationType) {

		// syncPartyProcessor.removeRelation(parentRefId, parentGroupType,
		// childRefId, childGroupType, relationType);
		logger.debug(String.format(
				"Remove relation with parent refId = %1s," + "parent type = %2s, " + "child refId = %3s, "
						+ "child type = %4s, " + "relation type = %5s",
				parentRefId, parentGroupType, childRefId, childGroupType, relationType));
	}

}
