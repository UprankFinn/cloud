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

    ProxyServerRequestPacket,

    ProxyRegisteredPacket,
    ProxyUnRegisteredPacket,

    ProxySyncServersPacket,

    ProxyAddGameServerPacket,
    ProxyRemoveGameServerPacket,

    ProxyServerStartPacket,
    ProxyServerAlivePacket,
    ProxyServerStopPacket,

    ProxyServerUpdatePacket,

    ProxyToggleMaintenancePacket,

    //========= Server ============

    GameServerRequestPacket,
    GameServerRequestStopPacket,

    GameServerStartPacket,
    GameServerAlivePacket,
    GameServerStopPacket,

    GameServerUpdatePacket,

    GameServerSignPacket,
    GameServerSignRequestPacket,

    //========= SYNC ==============

    ProxySyncPacket,
    GetProxySyncPacket,

    //========= Player ============

    PlayerLoginPacket,
    PlayerLogOutPacket,

    PlayerMessagePacket,

    PlayerSwitchServerPacket,

    GetCloudPlayerPacket,

    ;

}
