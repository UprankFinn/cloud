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

    CloudStopPacket,

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

    GameServerSyncPacket,
    GameServerUnsyncPacket,

    GameServerUpdatePlayerPacket,

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

    //========= Friend ============

    PlayerAddFriendPacket,
    PlayerAcceptFriendPacket,

    PlayerRemoveFriendPacket,
    FriendMessagePacket,

    GetFriendPlayerPacket,

    ;

}
