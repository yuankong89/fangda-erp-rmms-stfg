package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.DefinitionDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Mapper
interface DefinitionDao {
    fun getByDefType(defType: String): DefinitionDO
    fun insertDefinition(definitionDO: DefinitionDO): Int
}