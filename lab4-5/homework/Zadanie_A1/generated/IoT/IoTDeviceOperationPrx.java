//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.9
//
// <auto-generated>
//
// Generated from file `iot.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package IoT;

public interface IoTDeviceOperationPrx extends com.zeroc.Ice.ObjectPrx
{
    default java.util.Map<java.lang.String, java.lang.String> getInfo()
    {
        return getInfo(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default java.util.Map<java.lang.String, java.lang.String> getInfo(java.util.Map<String, String> context)
    {
        return _iceI_getInfoAsync(context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.util.Map<java.lang.String, java.lang.String>> getInfoAsync()
    {
        return _iceI_getInfoAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.util.Map<java.lang.String, java.lang.String>> getInfoAsync(java.util.Map<String, String> context)
    {
        return _iceI_getInfoAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.util.Map<java.lang.String, java.lang.String>> _iceI_getInfoAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.util.Map<java.lang.String, java.lang.String>> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getInfo", com.zeroc.Ice.OperationMode.Idempotent, sync, null);
        f.invoke(true, context, null, null, istr -> {
                     java.util.Map<java.lang.String, java.lang.String> ret;
                     ret = DeviceInfoHelper.read(istr);
                     return ret;
                 });
        return f;
    }

    default void changeName(String name)
    {
        changeName(name, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void changeName(String name, java.util.Map<String, String> context)
    {
        _iceI_changeNameAsync(name, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<Void> changeNameAsync(String name)
    {
        return _iceI_changeNameAsync(name, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> changeNameAsync(String name, java.util.Map<String, String> context)
    {
        return _iceI_changeNameAsync(name, context, false);
    }

    /**
     * @hidden
     * @param iceP_name -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_changeNameAsync(String iceP_name, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "changeName", null, sync, null);
        f.invoke(false, context, null, ostr -> {
                     ostr.writeString(iceP_name);
                 }, null);
        return f;
    }

    default void changeSettings(java.util.Map<java.lang.String, java.lang.Short> settings)
        throws IllegalSettingValueException,
               UnrecognisedSettingException
    {
        changeSettings(settings, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void changeSettings(java.util.Map<java.lang.String, java.lang.Short> settings, java.util.Map<String, String> context)
        throws IllegalSettingValueException,
               UnrecognisedSettingException
    {
        try
        {
            _iceI_changeSettingsAsync(settings, context, true).waitForResponseOrUserEx();
        }
        catch(IllegalSettingValueException ex)
        {
            throw ex;
        }
        catch(UnrecognisedSettingException ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> changeSettingsAsync(java.util.Map<java.lang.String, java.lang.Short> settings)
    {
        return _iceI_changeSettingsAsync(settings, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> changeSettingsAsync(java.util.Map<java.lang.String, java.lang.Short> settings, java.util.Map<String, String> context)
    {
        return _iceI_changeSettingsAsync(settings, context, false);
    }

    /**
     * @hidden
     * @param iceP_settings -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_changeSettingsAsync(java.util.Map<java.lang.String, java.lang.Short> iceP_settings, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "changeSettings", null, sync, _iceE_changeSettings);
        f.invoke(true, context, null, ostr -> {
                     SettingsHelper.write(ostr, iceP_settings);
                 }, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_changeSettings =
    {
        IllegalSettingValueException.class,
        UnrecognisedSettingException.class
    };

    default void returnToFactorySettings()
    {
        returnToFactorySettings(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void returnToFactorySettings(java.util.Map<String, String> context)
    {
        _iceI_returnToFactorySettingsAsync(context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<Void> returnToFactorySettingsAsync()
    {
        return _iceI_returnToFactorySettingsAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> returnToFactorySettingsAsync(java.util.Map<String, String> context)
    {
        return _iceI_returnToFactorySettingsAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_returnToFactorySettingsAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "returnToFactorySettings", null, sync, null);
        f.invoke(false, context, null, null, null);
        return f;
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static IoTDeviceOperationPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static IoTDeviceOperationPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static IoTDeviceOperationPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static IoTDeviceOperationPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static IoTDeviceOperationPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static IoTDeviceOperationPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, IoTDeviceOperationPrx.class, _IoTDeviceOperationPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default IoTDeviceOperationPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (IoTDeviceOperationPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default IoTDeviceOperationPrx ice_adapterId(String newAdapterId)
    {
        return (IoTDeviceOperationPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default IoTDeviceOperationPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (IoTDeviceOperationPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default IoTDeviceOperationPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (IoTDeviceOperationPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default IoTDeviceOperationPrx ice_invocationTimeout(int newTimeout)
    {
        return (IoTDeviceOperationPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default IoTDeviceOperationPrx ice_connectionCached(boolean newCache)
    {
        return (IoTDeviceOperationPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default IoTDeviceOperationPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (IoTDeviceOperationPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default IoTDeviceOperationPrx ice_secure(boolean b)
    {
        return (IoTDeviceOperationPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default IoTDeviceOperationPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (IoTDeviceOperationPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default IoTDeviceOperationPrx ice_preferSecure(boolean b)
    {
        return (IoTDeviceOperationPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default IoTDeviceOperationPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (IoTDeviceOperationPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default IoTDeviceOperationPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (IoTDeviceOperationPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default IoTDeviceOperationPrx ice_collocationOptimized(boolean b)
    {
        return (IoTDeviceOperationPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default IoTDeviceOperationPrx ice_twoway()
    {
        return (IoTDeviceOperationPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default IoTDeviceOperationPrx ice_oneway()
    {
        return (IoTDeviceOperationPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default IoTDeviceOperationPrx ice_batchOneway()
    {
        return (IoTDeviceOperationPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default IoTDeviceOperationPrx ice_datagram()
    {
        return (IoTDeviceOperationPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default IoTDeviceOperationPrx ice_batchDatagram()
    {
        return (IoTDeviceOperationPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default IoTDeviceOperationPrx ice_compress(boolean co)
    {
        return (IoTDeviceOperationPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default IoTDeviceOperationPrx ice_timeout(int t)
    {
        return (IoTDeviceOperationPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default IoTDeviceOperationPrx ice_connectionId(String connectionId)
    {
        return (IoTDeviceOperationPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default IoTDeviceOperationPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (IoTDeviceOperationPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::IoT::IoTDeviceOperation";
    }
}
