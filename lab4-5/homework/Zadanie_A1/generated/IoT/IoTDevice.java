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

public class IoTDevice extends com.zeroc.Ice.Value
{
    public IoTDevice()
    {
        this.name = "";
        this.brand = "";
        this.model = "";
        this.type = "";
    }

    public IoTDevice(String name, String brand, String model, String type)
    {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.type = type;
    }

    public String name;

    public String brand;

    public String model;

    public String type;

    public IoTDevice clone()
    {
        return (IoTDevice)super.clone();
    }

    public static String ice_staticId()
    {
        return "::IoT::IoTDevice";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 1480713314L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, true);
        ostr_.writeString(name);
        ostr_.writeString(brand);
        ostr_.writeString(model);
        ostr_.writeString(type);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        name = istr_.readString();
        brand = istr_.readString();
        model = istr_.readString();
        type = istr_.readString();
        istr_.endSlice();
    }
}