/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException

/**
 * @author yuhb
 * @date 2020/7/3
 */
inline fun assertPersistSuccess(success: Boolean, lazyMessage: () -> String): Boolean {
    if (!success) {
        val msg = lazyMessage()
        throw PersistObjectException(msg)
    } else {
        return true
    }
}