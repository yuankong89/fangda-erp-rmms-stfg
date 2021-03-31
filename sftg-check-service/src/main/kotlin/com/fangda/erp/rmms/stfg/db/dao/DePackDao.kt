package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackDetailDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackMachineDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackPhotoDO
import com.fangda.erp.rmms.stfg.db.query.DePackMachineQuery
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/11
 */
@Mapper
interface DePackDao {
    fun listDePackMachineByCriteria(query: DePackMachineQuery): List<DePackMachineDO>
    fun listDePackDetailById(rawMachineId: String): List<DePackDetailDO>
    fun countDePackPhotoById(rawMachineId: String): Int

    fun insertDePackData(dePackDataDO: DePackDataDO): Int
    fun updateDePackData(dePackDataDO: DePackDataDO): Int
    fun insertDePackDataDetail(dePackDetailDO: DePackDetailDO): Int
    fun insertDePackPhoto(dePackPhotoDO: DePackPhotoDO): Int

    fun deleteDePackDataDetail(rawMachineId: String): Int
    fun deleteDePackPhoto(rawMachineId: String): Int
}