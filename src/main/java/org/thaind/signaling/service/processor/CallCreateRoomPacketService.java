package org.thaind.signaling.service.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.hibernate.entity.UserEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.repository.RoomRepository;
import org.thaind.signaling.repository.UserRepository;
import org.thaind.signaling.repository.impl.RoomRepositoryImpl;
import org.thaind.signaling.repository.impl.UserRepositoryImpl;
import org.thaind.signaling.service.PacketService;

import java.util.UUID;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {
 * "toUser":"toUser"
 * }
 */
public class CallCreateRoomPacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("CallCreateRoomProcessor");

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final RoomRepository roomRepository = new RoomRepositoryImpl();

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
        String toUser = requestBody.optString("toUser");
        if (magic == 0) {
            UserEntity userEntity = userRepository.findById(toUser);
            if (userEntity == null) {
                magic = 1;
                msg = "User not found";
            }
        }
        RoomEntity roomEntity = roomRepository.findByFromAndToUser(userConnection.getUserId(), toUser);
        if (magic == 0 && roomEntity == null) {
            roomEntity = createNewRoom(userConnection.getUserId(), toUser);

        }
        if (magic == 0) {
            if (roomEntity == null) {
                msg = "Unable to create room";
            } else {
                resPacket.setField(ResponseField.ROOM_ID.getField(), roomEntity.getId());
            }
        }
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
        userConnection.sendPacket(resPacket);
    }

    RoomEntity createNewRoom(String creator, String withUser) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(UUID.randomUUID().toString());
        roomEntity.setCreator(creator);
        roomEntity.setWithUser(withUser);
        return roomRepository.save(roomEntity);
    }
}
