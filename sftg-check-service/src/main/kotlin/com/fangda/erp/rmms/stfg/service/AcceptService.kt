package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.libs.utils.JsonUtils
import com.fangda.erp.libs.utils.TimeUtils
import com.fangda.erp.rmms.stfg.bo.accept.AcceptData
import com.fangda.erp.rmms.stfg.bo.accept.AcceptDataDetail
import com.fangda.erp.rmms.stfg.bo.unload.UnloadData
import com.fangda.erp.rmms.stfg.dto.accept.AcceptDataDetailDto
import com.fangda.erp.rmms.stfg.dto.accept.AcceptDataDto
import com.fangda.erp.rmms.stfg.dto.unload.UnloadDataDto
import com.fangda.erp.rmms.stfg.manager.AcceptManager
import com.fangda.erp.rmms.stfg.manager.CheckManager
import com.fangda.erp.rmms.stfg.manager.UnloadManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.netty.ByteBufMono
import java.io.File

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Service
class AcceptService @Autowired constructor(
    private val unloadManager: UnloadManager,
    private val acceptManager: AcceptManager,
    private val checkManager: CheckManager
) {
    /**
     * 处理卸车数据
     */
    fun handleUnloadData(
        rawMachineId: String,
        unloadDataStr: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable { JsonUtils.parseObject(unloadDataStr, UnloadDataDto::class.java) }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .map { this.convertUnloadDataDtoToBO(it!!) }
            .doOnNext { it.rawMachineId = rawMachineId }
            .flatMap { Mono.fromCallable { unloadManager.newUnloadData(it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 处理中段验收数据
     */
    fun handleMidAcceptData(rawMachineId: String, acceptDataStr: String, operator: String): Mono<Boolean> {
        return Mono.fromCallable { JsonUtils.parseObject(acceptDataStr, AcceptDataDto::class.java) }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .map { convertAcceptDataDtoToBO(it!!) }
            .flatMap { Mono.fromCallable { acceptManager.newMidAcceptData(it, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 处理验收数据
     */
    fun handleAcceptData(
        rawMachineId: String,
        acceptDataStr: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable { JsonUtils.parseObject(acceptDataStr, AcceptDataDto::class.java) }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .map { this.convertAcceptDataDtoToBO(it!!) }
            .doOnNext { it.rawMachineId = rawMachineId }
            .flatMap { Mono.fromCallable { acceptManager.newAcceptData(it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 根据车辆[rawMachineId]来获取卸车数据
     */
    fun getUnloadData(rawMachineId: String): Mono<UnloadDataDto> {
        return Mono.fromCallable { unloadManager.getUnloadData(rawMachineId) }
            .map { convertUnloadDataBOToDto(it) }
    }

    /**
     * 获得指定的卸车照片
     */
    fun getUnloadPhoto(rawMachineId: String, photoNo: Int): Mono<ByteArray> {
        return Mono.fromCallable { unloadManager.getUnloadPhoto(rawMachineId, photoNo) }
            .flatMap { ByteBufMono.just(it.data) }
    }

    /**
     * 获得指定的验收照片
     */
    fun getAcceptPhoto(rawMachineId: String, photoNo: Int): Mono<ByteArray> {
        return Mono.fromCallable { acceptManager.getAcceptPhoto(rawMachineId, photoNo) }
            .flatMap { ByteBufMono.just(it.data) }
    }

    /**
     * 获得中段验收数据
     */
    fun getMidAcceptData(rawMachineId: String): Mono<AcceptDataDto> {
        return Mono.fromCallable { acceptManager.getMidAcceptData(rawMachineId) }
            .map { convertAcceptDataBOToDto(it) }
    }

    /**
     * 根据车辆[rawMachineId]来获取验收数据
     */
    fun getAcceptData(rawMachineId: String): Mono<AcceptDataDto> {
        return Mono.fromCallable { acceptManager.getAcceptData(rawMachineId) }
            .map { convertAcceptDataBOToDto(it) }
    }

    /**
     * 自我检查
     */
    fun handleAcceptSelfChecked(rawMachineId: String): Mono<Boolean> {
        return Mono.fromCallable { acceptManager.selfCheckAccept(rawMachineId) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 自我检查
     */
    fun handleUnloadAcceptSelfChecked(rawMachineId: String): Mono<Boolean> {
        return Mono.fromCallable { unloadManager.selfCheckUnload(rawMachineId) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 修改卸车数据
     */
    fun handleModifyUnloadData(
        rawMachineId: String,
        unloadDataStr: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable { JsonUtils.parseObject(unloadDataStr, UnloadDataDto::class.java) }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .map { this.convertUnloadDataDtoToBO(it!!) }
            .doOnNext { it.rawMachineId = rawMachineId }
            .flatMap { Mono.fromCallable { unloadManager.modifyUnloadData(it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 修改验收数据
     */
    fun handleModifyAcceptData(
        rawMachineId: String,
        acceptDataStr: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable { JsonUtils.parseObject(acceptDataStr, AcceptDataDto::class.java) }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .map { this.convertAcceptDataDtoToBO(it!!) }
            .doOnNext { it.rawMachineId = rawMachineId }
            .flatMap { Mono.fromCallable { acceptManager.modifyAcceptData(it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 上传审核数据
     */
    fun handleCheck(rawMachineId: String, result: String, comment: String, operator: String): Mono<Boolean> {
        return if (result == "approve") {
            Mono.fromCallable { checkManager.acceptApprove(rawMachineId, comment, operator) }
        } else {
            Mono.fromCallable { checkManager.acceptDeny(rawMachineId, comment, operator) }
        }
    }

    // ---- private ----
    private fun convertUnloadDataDtoToBO(data: UnloadDataDto): UnloadData {
        return UnloadData().apply {
            this.unloadPos = data.unloadPos
            this.unloadDate = TimeUtils.parse(data.unloadDate, "yyyy-MM-dd HH:mm:ss")
            this.unloadRemark = data.unloadRemark
        }
    }

    private fun convertUnloadDataBOToDto(data: UnloadData): UnloadDataDto {
        return UnloadDataDto().apply {
            this.unloadPos = data.unloadPos
            this.unloadDate = TimeUtils.format(data.unloadDate, "yyyy-MM-dd HH:mm:ss")
            this.unloadRemark = data.unloadRemark
        }
    }

    private fun convertAcceptDataDtoToBO(data: AcceptDataDto): AcceptData {
        return AcceptData().apply {
            this.acceptPos = data.acceptPos
            this.acceptDate = TimeUtils.parse(data.acceptDate, "yyyy-MM-dd HH:mm:ss")
            this.acceptRemark = data.acceptRemark
            this.details = data.details.map(this@AcceptService::convertAcceptDataDetailDtoToBO)
        }
    }

    private fun convertAcceptDataDetailDtoToBO(detail: AcceptDataDetailDto): AcceptDataDetail {
        return AcceptDataDetail().apply {
            this.dataNo = detail.dataNo
            this.dataName = detail.dataName
            this.dataValue = detail.dataValue.toDouble()
        }
    }

    private fun convertAcceptDataBOToDto(data: AcceptData): AcceptDataDto {
        return AcceptDataDto().apply {
            this.rawMachineId = data.rawMachineId
            this.acceptPos = data.acceptPos
            this.acceptDate = TimeUtils.format(data.acceptDate, "yyyy-MM-dd HH:mm:ss")
            this.acceptRemark = data.acceptRemark
            this.photoLinkList = mutableListOf<String>().apply {
                for (i in 1..data.acceptPhotoCount) {
                    this.add("/accept/${data.rawMachineId}/photos/$i")
                }
                for (i in 1..data.dePackPhotoCount) {
                    this.add("/depack/${data.rawMachineId}/photos/$i")
                }
            }
            this.details = data.details.map { convertAcceptDataDetailBOToDto(it) }
        }
    }

    private fun convertAcceptDataDetailBOToDto(detail: AcceptDataDetail): AcceptDataDetailDto {
        return AcceptDataDetailDto().apply {
            this.dataNo = detail.dataNo
            this.dataName = detail.dataName
            this.dataValue = detail.dataValue.toString()
        }
    }
}