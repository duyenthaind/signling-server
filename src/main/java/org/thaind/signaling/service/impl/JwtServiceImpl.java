package org.thaind.signaling.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thaind.signaling.bootstrap.SingletonHolder;
import org.thaind.signaling.common.AppConfig;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.service.JwtService;

import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * @author duyenthaind
 */
public class JwtServiceImpl implements JwtService {

    private static final Logger LOGGER = LogManager.getLogger("JwtService");

    @Override
    public Response verifyAccessToken(String accessToken) {
        AppConfig appConfig = SingletonHolder.getBeanOrDefault(AppConfig.class);
        if (appConfig != null) {
            Key hMacKey = new SecretKeySpec(appConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
            try {
                Jwts.parser().setSigningKey(hMacKey).parseClaimsJws(accessToken);
            } catch (InvalidKeyException ex) {
                return Response.forbidden(Constants.JWTResponseAuthen.ALG_UNSUPPORTED.toString());
            } catch (ExpiredJwtException ex) {
                return Response.forbidden(Constants.JWTResponseAuthen.EXPIRED.toString());
            } catch (Exception ex) {
                return Response.forbidden(Constants.JWTResponseAuthen.UNDEFINED.toString());
            }
        }
        return Response.ok();
    }

    @Override
    public Claims parseAllClaims(String accessToken) {
        AppConfig appConfig = SingletonHolder.getBeanOrDefault(AppConfig.class);
        if (appConfig != null) {
            try {
                Key hMacKey = new SecretKeySpec(appConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
                return Jwts.parser().setSigningKey(hMacKey).parseClaimsJws(accessToken).getBody();
            } catch (Exception ex) {
                LOGGER.error("Parse error ", ex);
            }
        }
        return null;
    }
}
