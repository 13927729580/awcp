package cn.org.awcp.metadesigner.util;

import org.apache.commons.lang3.StringUtils;

import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;

public class UpdateColumn {

	public static String updateColumn(MetaModelItemsVO newMetaModelItem, String tableName) {
		// alter table fw_mm_metamodelitems change itemCode itemCode varchar(20) not
		// null default '123456';
		StringBuffer buffer = new StringBuffer();
		buffer.append("alter table " + tableName);
		buffer.append(" change " + newMetaModelItem.getItemCode() + " " + newMetaModelItem.getItemCode());
		bulidColumn(newMetaModelItem, buffer);
		return buffer.toString();
	}

	// 添加列
	public static String newAddColumn(MetaModelItemsVO mmi, String tableName) {
		// alter table user add COLUMN new1 VARCHAR(20) not null DEFAULT NULL
		StringBuffer buffer = new StringBuffer();
		buffer.append("alter table " + tableName);
		buffer.append(" add column " + mmi.getItemCode());
		bulidColumn(mmi, buffer);
		return buffer.toString();
	}

	public static String deleteColumn(String tableName, String columnName) {
		return "alter table " + tableName + "  drop column " + columnName;
	}

	public static void bulidColumn(MetaModelItemsVO mmi, StringBuffer buffer) {
		if (StringUtils.isNotBlank(mmi.getItemLength())) {
			buffer.append(" " + mmi.getItemType() + "(" + mmi.getItemLength() + ") ");
		} else {
			buffer.append(" " + mmi.getItemType());
		}
		if (mmi.getUseNull() != null && mmi.getUseNull() == 0) {
			buffer.append(" not null ");
		}
		if (mmi.getUsePrimaryKey() != null && mmi.getUsePrimaryKey() == 1) {

			buffer.append(" primary key ");
			if (mmi.getItemType().contains("int")) {

				buffer.append(" auto_increment ");
			}
		}
		if (mmi.getUseIndex() != null) {
			if (mmi.getUseIndex() == 1) {
				buffer.append(" UNIQUE key ");
			} else if (mmi.getUseIndex() == 2) {
				buffer.append(" index(" + mmi.getItemCode() + ") ");
			}
		}
		if (StringUtils.isNotBlank(mmi.getDefaultValue())) {
			if (mmi.getItemType().contains("char")) {
				buffer.append(" default '" + mmi.getDefaultValue() + "' ");
			} else {
				buffer.append(" default " + mmi.getDefaultValue());
			}
		}
		if (StringUtils.isNotBlank(mmi.getRemark())) {
			buffer.append(" comment '" + mmi.getRemark() + "'");
		}

	}

}
