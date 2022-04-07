package org.thaind.signaling.service.processor;

/**
 * @author duyenthai
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.RoomCallManager;
import org.thaind.signaling.dto.CallRoom;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.repository.RoomRepository;
import org.thaind.signaling.repository.impl.RoomRepositoryImpl;
import org.thaind.signaling.service.PacketService;

import static org.thaind.signaling.common.Constants.ResponseField;

import java.util.Optional;

/**
 * {"roomId","roomId"}
 */
public class JoinRoomPacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("JoinRoomPacketProcessor");

    private final RoomRepository repository = new RoomRepositoryImpl();

    @Override
    public void processPacket(Packet packet, UserConnection userConnection) {
        Packet resPacket = new Packet();
        resPacket.setServiceType(packet.getServiceType());
        JSONObject requestBody = packet.getBody();
        if (!Processor.validateEmptyRequest(packet, userConnection)) {
            return;
        }
        int magic = 0;
        String msg = "success";
        if (!userConnection.isForCall()) {
            magic = -1;
            msg = "Not for call";
        }
        if (userConnection.getCallRoom() != null) {
            magic = -2;
            msg = "User in another room";
        }
        String roomId = requestBody.optString("roomId", "");
        if (magic == 0 && StringUtils.isEmpty(roomId)) {
            LOGGER.error("RoomId not found in payload, this request is malformed");
            magic = 1;
            msg = "RoomId is invalid";
        }
        Optional<CallRoom> room = Optional.empty();
        if (magic == 0) {
            room = getOrCreateNewRoom(roomId, userConnection);
            if (room.isEmpty()) {
                magic = 2;
                msg = "Room not found";
            }
        }
        if (magic == 0 && room.isPresent()) {
            Response internalResponse = room.get().userJoinRoom(userConnection);
            if (!internalResponse.equals(Response.ok())) {
                magic = internalResponse.getRes();
                msg = internalResponse.getMessage();
            } else {
                onJoinRoomOk(resPacket, userConnection, room.get());
            }
        }
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
    }

    Optional<CallRoom> getOrCreateNewRoom(String roomId, UserConnection userConnection) {
        CallRoom room = RoomCallManager.getInstance().getCallRoom(roomId);
        if (room != null) {
            return Optional.of(room);
        }
        RoomEntity roomEntity = repository.findById(roomId);
        if (roomEntity == null) {
            LOGGER.error(String.format("Cannot find room with id %s", roomId));
            return Optional.empty();
        }
        if (roomEntity.getCreator().equals(userConnection.getUserId())) {
            return Optional.of(new CallRoom(roomId));
        }
        LOGGER.error(String.format("Room %s is discarded, joiner do not have permission to create new", roomId));
        return Optional.empty();
    }

    void onJoinRoomOk(Packet resPacket, UserConnection userConnection, CallRoom room) {
        LOGGER.info(String.format("User %s joined room with id %s ", userConnection.getUserId(), room.getRoomId()));
        resPacket.setField(ResponseField.ROOM_ID.getField(), room.getRoomId());
        JSONObject customData = new JSONObject();
        customData.put("joined", true);
        resPacket.setField(ResponseField.DATA.getField(), customData);
        userConnection.setCallRoom(room);
    }
}
