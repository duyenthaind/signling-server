package org.thaind.signaling.service.processor;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.exception.TooManyDeviceException;
import org.thaind.signaling.hibernate.entity.UserEntity;
import org.thaind.signaling.repository.UserRepository;
import org.thaind.signaling.repository.impl.UserRepositoryImpl;
import org.thaind.signaling.service.JwtService;
import org.thaind.signaling.service.PacketService;
import org.thaind.signaling.service.impl.JwtServiceImpl;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {
 * "accessToken":"accessToken",
 * "isForCall":true/false
 * }
 */
public class AuthenticationPacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("AuthenticationPacketProcessor");

    private final JwtService jwtService = new JwtServiceImpl();
    private final UserRepository repository = new UserRepositoryImpl();

    @Override
    public void processPacket(Packet packet, UserConnection userConnection) {
        Packet resPacket = new Packet();
        packet.setServiceType(packet.getServiceType());
        AuthCode authCode = AuthCode.INIT;
        JSONObject body = packet.getBody();
        String accessToken = body == null ? "" : body.optString("accessToken", "");
        if (body == null || StringUtils.isEmpty(accessToken)) {
            authCode = AuthCode.ACCESS_TOKEN_EMPTY;
        }
        if (authCode == AuthCode.INIT) {
            Response jwtRes = jwtService.verifyAccessToken(accessToken);
            if (!jwtRes.equals(Response.ok())) {
                authCode = AuthCode.ACCESS_TOKEN_CANNOT_BE_DECODE;
                packet.setField(ResponseField.MESSAGE.getField(), jwtRes.getMessage());
            }
        }
        Claims claims = null;
        if (authCode == AuthCode.INIT) {
            claims = jwtService.parseAllClaims(accessToken);
            if (claims == null) {
                authCode = AuthCode.ACCESS_TOKEN_CANNOT_BE_DECODE;
            }

        }
        if (authCode == AuthCode.INIT) {
            String userId = claims != null ? claims.get("userId", String.class) : "";
            if (StringUtils.isEmpty(userId)) {
                LOGGER.error("Cannot parse userId from token, this is regarded as authentication phase failed");
                authCode = AuthCode.ACCESS_TOKEN_CANNOT_BE_DECODE;
            }
            if (authCode == AuthCode.INIT) {
                UserEntity user = repository.findById(userId);
                if (user == null) {
                    LOGGER.error(String.format("Not found user with id %s", userId));
                    authCode = AuthCode.USER_NOT_FOUND;
                } else {
                    userConnection.setUserId(userId);
                    boolean isForCall = body != null && body.optBoolean("isForCall", false);
                    userConnection.setForCall(isForCall);
                    try {
                        userConnection.authenticate();
                    } catch (TooManyDeviceException ex) {
                        LOGGER.error("Too many device for user " + userId, ex);
                        userConnection.setUserId(null);
                        userConnection.setForCall(false);
                        authCode = AuthCode.TOO_MANY_DEVICE;
                    }
                }
            }
        }
        if (authCode == AuthCode.INIT) {
            authCode = AuthCode.SUCCESS;
        }
        resPacket.setField(ResponseField.RES.getField(), authCode.value);
        userConnection.sendPacket(resPacket);
    }

    public enum AuthCode {
        INIT(-1),
        SUCCESS(0),
        ACCESS_TOKEN_EMPTY(1),
        ACCESS_TOKEN_CANNOT_BE_DECODE(2),
        USER_ID_INVALID(3),
        UNDEFINED(4),
        TOO_MANY_DEVICE(5),
        USER_NOT_FOUND(6),
        ;
        private final int value;

        AuthCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
