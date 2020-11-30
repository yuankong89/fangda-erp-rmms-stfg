package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.Definition
import com.fangda.erp.rmms.stfg.db.dao.DefinitionDao
import com.fangda.erp.rmms.stfg.db.dataobject.DefinitionDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Service
class DefinitionManager @Autowired constructor(
    private val definitionDao: DefinitionDao
) {
    /**
     * 通过[defType]获得数据
     */
    fun getDefinitionByDefType(defType: String): Definition {
        return this.convertDefinitionDOToBo(definitionDao.selectByDefType(defType))
    }

    /**
     * IO
     * 修改数据
     */
    @Throws(PersistObjectException::class)
    fun modifyDefinition(defType: String, defData: String): Boolean {
        return assertPersistSuccess(definitionDao.insertDefinition(DefinitionDO().apply {
            this.defType = defType
            this.defData = defData
        }) == 1) {
            "插入Definition表出现错误"
        }
    }

    // ---- private ----
    fun convertDefinitionDOToBo(definitionDO: DefinitionDO): Definition {
        return Definition().apply {
            this.defType = definitionDO.defType
            this.defData = definitionDO.defData
            this.defVersion = definitionDO.defVersion
        }
    }

    companion object {
        lateinit var YK_TYPE_DEF: Definition
        lateinit var FG_TYPE_DEF: Definition
        lateinit var ST_TYPE_DEF: Definition

        fun getAllTypes(): String {
            return YK_TYPE_DEF.defData + "," + FG_TYPE_DEF.defData + "," + ST_TYPE_DEF.defData
        }
    }

    @PostConstruct
    private fun init() {
        YK_TYPE_DEF = this.getDefinitionByDefType("YK")
        FG_TYPE_DEF = this.getDefinitionByDefType("FG")
        ST_TYPE_DEF = this.getDefinitionByDefType("ST")
    }
}