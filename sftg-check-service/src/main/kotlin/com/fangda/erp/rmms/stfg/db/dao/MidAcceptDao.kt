package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDetailDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Mapper
interface MidAcceptDao {
    // 中段验收提交
    fun insertMidAcceptData(acceptDataDO: AcceptDataDO): Int
    fun insertMidAcceptDataDetail(acceptDetailDO: AcceptDetailDO): Int

    // 查询中段验收数据
    fun getMidAcceptData(rawMachineId: String): AcceptDataDO
    fun listMidAcceptDataDetails(rawMachineId: String): List<AcceptDetailDO>
}