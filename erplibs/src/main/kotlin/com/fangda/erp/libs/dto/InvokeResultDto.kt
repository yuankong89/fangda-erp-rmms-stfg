/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.libs.dto

/**
 * @author yuhb
 * @date 2020/5/19
 */
class InvokeResultDto {
    var isSuccess = false
    var message: String? = null

    companion object {
        const val SERVER_ERROR = 570
        fun successResult(): InvokeResultDto {
            return InvokeResultDto().apply {
                this.isSuccess = true
            }
        }

        fun successResult(msg: String): InvokeResultDto {
            return InvokeResultDto().apply {
                this.isSuccess = true
                this.message = msg
            }
        }

        fun failResult(): InvokeResultDto {
            return InvokeResultDto().apply {
                this.isSuccess = false
            }
        }

        fun failResult(msg: String): InvokeResultDto {
            return InvokeResultDto().apply {
                this.isSuccess = false
                this.message = msg
            }
        }
    }
}