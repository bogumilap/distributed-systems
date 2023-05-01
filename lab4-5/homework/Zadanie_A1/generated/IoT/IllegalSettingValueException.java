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

public class IllegalSettingValueException extends com.zeroc.Ice.UserException
{
    public IllegalSettingValueException()
    {
        this.reason = "";
    }

    public IllegalSettingValueException(Throwable cause)
    {
        super(cause);
        this.reason = "";
    }

    public IllegalSettingValueException(String reason)
    {
        this.reason = reason;
    }

    public IllegalSettingValueException(String reason, Throwable cause)
    {
        super(cause);
        this.reason = reason;
    }

    public String ice_id()
    {
        return "::IoT::IllegalSettingValueException";
    }

    public String reason;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::IoT::IllegalSettingValueException", -1, true);
        ostr_.writeString(reason);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        reason = istr_.readString();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = 1064800790L;
}