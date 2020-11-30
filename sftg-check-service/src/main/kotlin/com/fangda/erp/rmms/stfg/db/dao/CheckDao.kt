package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptCheckRecordDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Mapper
interface CheckDao {
    fun backUpAcceptData(rawMachineId: String)
    fun backUpAcceptDataDetail(rawMachineId: String)

    fun insertAcceptCheckRecord(acceptCheckRecordDO: AcceptCheckRecordDO): Int
}