package de.uprank.cloud.packets;

public enum PacketType {

    ERROR,
    GROUP_LOADED_PACKET,

    WrapperAlivePacket,
    WrapperStartPacket,
    WrapperStopPacket,
    WrapperStopServerPacket,

    GroupCreatePacket,
    GroupDeletePacket,

    TemplateCreatePacket,
    TemplateDeletePacket,

    GameServerRequestPacket,
    GameServerRequestStopPacket,

    ProxyAddGameServerPacket,
    ProxyRemoveGameServerPacket,

    ProxyServerStartPacket,
    ProxyServerStopPacket,

    GameServerStartPacket,
    GameServerAlivePacket,
    GameServerStopPacket,

    ;

}
