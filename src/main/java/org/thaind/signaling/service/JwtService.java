package org.thaind.signaling.service;

import io.jsonwebtoken.Claims;
import org.thaind.signaling.dto.internal.protocol.Response;

/**
 * @author duyenthaind
 */
public interface JwtService {
    Response verifyAccessToken(String accessToken);

    Claims parseAllClaims(String accessToken);
}
