package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDetailDO
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptPhotoDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Mapper
interface AcceptDao {
    // 新增验收数据
    fun insertAcceptData(acceptDataDO: AcceptDataDO): Int
    fun insertAcceptDataDetail(acceptDetailDO: AcceptDetailDO): Int
    fun insertAcceptPhoto(acceptPhotoDO: AcceptPhotoDO): Int

    // 查询验收数据
    fun selectAcceptData(rawMachineId: String): AcceptDataDO
    fun listAcceptDataDetails(rawMachineId: String): List<AcceptDetailDO>

    // 修改验收数据
    fun updateAcceptData(acceptDataDO: AcceptDataDO): Int
    fun deleteAcceptPhotos(rawMachineId: String): Int
    fun deleteAcceptDataDetails(rawMachineId: String): Int

    // 审核
    fun deleteAcceptData(rawMachineId: String): Int
    fun updateAcceptDataState(rawMachineId: String, state: Int): Int

    // 自我检查
    fun insertSelfCheckFlag(rawMachineId: String): Int
    fun countSelfCheckFlag(rawMachineId: String): Int
    fun deleteSelfCheckFlag(rawMachineId: String): Int

    // 验收照片
    fun selectAcceptPhoto(rawMachineId: String, photoNo: Int): AcceptPhotoDO
    fun countAcceptPhotoById(rawMachineId: String): Int
}