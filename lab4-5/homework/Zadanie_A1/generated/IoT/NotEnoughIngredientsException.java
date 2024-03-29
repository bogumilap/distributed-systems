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

public class NotEnoughIngredientsException extends com.zeroc.Ice.UserException
{
    public NotEnoughIngredientsException()
    {
        this.reason = "";
    }

    public NotEnoughIngredientsException(Throwable cause)
    {
        super(cause);
        this.reason = "";
    }

    public NotEnoughIngredientsException(String reason)
    {
        this.reason = reason;
    }

    public NotEnoughIngredientsException(String reason, Throwable cause)
    {
        super(cause);
        this.reason = reason;
    }

    public String ice_id()
    {
        return "::IoT::NotEnoughIngredientsException";
    }

    public String reason;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::IoT::NotEnoughIngredientsException", -1, true);
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
    public static final long serialVersionUID = 36557072L;
}
