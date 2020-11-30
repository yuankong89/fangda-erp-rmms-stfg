/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fangda.erp.authlibs.exception.BadJwtTokenException
import com.fangda.erp.libs.exception.SystemException
import java.io.UnsupportedEncodingException
import java.time.Instant
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * @author yuhb
 * @date 2020/6/2
 */
object JwtUtils {
    private const val ALGORITHM_KEY = "secret"
    private const val SUBJECT = "fangda"
    private const val ISSUER = "fangda"

    private val algorithm: Algorithm = Algorithm.HMAC256(ALGORITHM_KEY)
    private val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(ISSUER).build()

    fun createJWT(claims: Map<String, String>, expirePeriod: Period = Period.ofDays(14)): String {
        return try {
            JWT.create().apply {
                withSubject(SUBJECT)
                claims.forEach {
                    withClaim(it.key, it.value)
                }
                withExpiresAt(Date.from(Instant.now().plus(expirePeriod.get(ChronoUnit.DAYS), ChronoUnit.DAYS)))
                withIssuer(ISSUER)
            }
                .sign(algorithm)
        } catch (e: UnsupportedEncodingException) {
            throw SystemException("${e.message}")
        } catch (e: JWTCreationException) {
            throw SystemException("${e.message}")
        }
    }

    fun validateJWT(jwtToken: String): DecodedJWT {
        return try {
            verifier.verify(jwtToken)
        } catch (e: JWTVerificationException) {
            throw BadJwtTokenException("${e.message}")
        }
    }
}