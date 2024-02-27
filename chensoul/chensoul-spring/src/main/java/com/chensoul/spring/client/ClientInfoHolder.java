package com.chensoul.spring.client;

/**
 * Makes the {@link ClientInfo} available to the thread.
 */
public class ClientInfoHolder {

    private static final ThreadLocal<ClientInfo> CLIENT_INFO_HOLDER = new InheritableThreadLocal<ClientInfo>();

    /**
     * Sets client info.
     *
     * @param clientInfo the client info
     */
    public static void setClientInfo(final ClientInfo clientInfo) {
        CLIENT_INFO_HOLDER.set(clientInfo);
    }

    /**
     * Gets client info.
     *
     * @return the client info
     */
    public static ClientInfo getClientInfo() {
        return CLIENT_INFO_HOLDER.get();
    }

    /**
     * Clear.
     */
    public static void clear() {
        CLIENT_INFO_HOLDER.remove();
    }
}
