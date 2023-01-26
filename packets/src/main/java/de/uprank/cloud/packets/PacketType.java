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

    //========= PROXY ============

    ProxyRequestPacket,

    ProxyRegisteredPacket,
    ProxyUnRegisteredPacket,

    ProxySyncServersPacket,

    ProxyAddGameServerPacket,
    ProxyRemoveGameServerPacket,

    ProxyServerStartPacket,
    ProxyServerAlivePacket,
    ProxyServerStopPacket,

    //========= Server ============

    GameServerRequestPacket,
    GameServerRequestStopPacket,

    GameServerStartPacket,
    GameServerAlivePacket,
    GameServerStopPacket,

    GameServerSignPacket,

    //========= Player ============

    PlayerLoginPacket,
    PlayerLogOutPacket,

    ;

}
