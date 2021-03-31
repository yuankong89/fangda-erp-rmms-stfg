package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.unload.UnloadDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.unload.UnloadDetailDO
import com.fangda.erp.rmms.stfg.db.dataobject.unload.UnloadPhotoDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Mapper
interface UnloadDao {
    // 卸车验收
    fun insertUnloadData(unloadDataDO: UnloadDataDO): Int
    fun insertUnloadPhoto(unloadPhotoDO: UnloadPhotoDO): Int

    // 查询卸车数据
    fun getUnloadData(rawMachineId: String): UnloadDataDO

    // 修改卸车数据
    fun updateUnloadData(unloadDataDO: UnloadDataDO): Int
    fun deleteUnloadPhotos(rawMachineId: String): Int

    // 审核卸车数据
    fun deleteUnloadData(rawMachineId: String): Int

    // 自我检查
    fun insertSelfCheckFlag(rawMachineId: String): Int
    fun countSelfCheckFlag(rawMachineId: String): Int
    fun deleteSelfCheckFlag(rawMachineId: String): Int

    // 查看卸车照片
    fun getUnloadPhoto(rawMachineId: String, photoNo: Int): UnloadPhotoDO
}