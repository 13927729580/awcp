package cn.org.awcp.metadesigner.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.org.awcp.metadesigner.application.MetaModelItemService;
import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;
import cn.org.awcp.metadesigner.vo.MetaModelVO;

@Component("createTables")
public class CreateTables {

	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	public String getSql(MetaModelVO mm, List<MetaModelItemsVO> mmi) {
		StringBuffer buffer = new StringBuffer("create table ").append(mm.getTableName()).append("(\n ");
		if (!mmi.isEmpty()) {
			boolean hasPri = false;
			for (MetaModelItemsVO mv : mmi) {
				buffer.append(mv.getItemCode() + " ");
				bulidColumn(mv, buffer);
				buffer.append(",\n");
				// 添加索引
				if (mv.getUseIndex() != null) {
					if (mv.getUseIndex() == 1) {
						buffer.append(" UNIQUE INDEX unique_" + mv.getItemCode() + " (`" + mv.getItemCode() + "`),\n");
					} else if (mv.getUseIndex() == 2) {
						buffer.append(" INDEX index_" + mv.getItemCode() + " (`" + mv.getItemCode() + "`),\n");
					}
				}
				if (mv.getUsePrimaryKey() != null && mv.getUsePrimaryKey() == 1) {
					hasPri = true;
				}
				metaModelItemsServiceImpl.save(mv);
			}
			// 没有主键则自动生成一个主键列
			if (!hasPri) {
				MetaModelItemsVO mv = getPrimaryItem(mm);
				buffer.append(" `" + mv.getItemCode() + "` ");
				bulidColumn(mv, buffer);
				buffer.append(",\n");
				metaModelItemsServiceImpl.save(mv);
			}
		} else {
			// 没有列则自动生成一个主键列
			MetaModelItemsVO mv = getPrimaryItem(mm);
			buffer.append(" `" + mv.getItemCode() + "` ");
			UpdateColumn.bulidColumn(mv, buffer);
			buffer.append(",\n");
			metaModelItemsServiceImpl.save(mv);
		}
		String s = buffer.toString().substring(0, buffer.toString().length() - 2);
		s += "\n)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		return s;
	}

	public static void bulidColumn(MetaModelItemsVO mmi, StringBuffer buffer) {
		if (StringUtils.isNotBlank(mmi.getItemLength())) {
			buffer.append(" " + mmi.getItemType() + "(" + mmi.getItemLength() + ") ");
		} else {
			buffer.append(" " + mmi.getItemType());
		}
		if (mmi.getUsePrimaryKey() != null && mmi.getUsePrimaryKey() == 1) {

			buffer.append(" primary key ");
			if (mmi.getItemType().contains("int")) {

				buffer.append(" auto_increment ");
			}
		}
		if (mmi.getUseNull() != null && mmi.getUseNull() == 0) {
			buffer.append(" not null ");
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

	private static MetaModelItemsVO getPrimaryItem(MetaModelVO mm) {
		MetaModelItemsVO mv = new MetaModelItemsVO();
		mv.setItemCode("ID");
		mv.setItemName("ID ");
		mv.setItemLength("11");
		mv.setRemark("主键");
		mv.setUseIndex(-1);
		mv.setUseNull(0);
		mv.setUsePrimaryKey(1);
		mv.setItemType("int");
		mv.setModelId(mm.getId());
		mv.setItemValid(1);
		return mv;
	}

}
