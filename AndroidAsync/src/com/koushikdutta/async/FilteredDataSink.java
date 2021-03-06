package com.koushikdutta.async;

import java.nio.ByteBuffer;

import junit.framework.Assert;

public class FilteredDataSink extends BufferedDataSink {
    public FilteredDataSink(DataSink sink) {
        super(sink);
        setMaxBuffer(0);
    }
    
    public ByteBufferList filter(ByteBufferList bb) {
        return bb;
    }

    @Override
    public final void write(ByteBuffer bb) {
        // don't filter and write if currently buffering, unless we know
        // that the buffer can fit the entirety of the filtered result
        if (isBuffering() && getMaxBuffer() != Integer.MAX_VALUE)
            return;
        ByteBufferList list = new ByteBufferList();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        Assert.assertTrue(bb.remaining() == 0);
        list.add(ByteBuffer.wrap(bytes));
        ByteBufferList filtered = filter(list);
        super.write(filtered, true);
    }

    @Override
    public final void write(ByteBufferList bb) {
        // don't filter and write if currently buffering, unless we know
        // that the buffer can fit the entirety of the filtered result
        if (isBuffering() && getMaxBuffer() != Integer.MAX_VALUE)
            return;
        ByteBufferList filtered = filter(bb);
        Assert.assertTrue(bb == null || filtered == bb || bb.remaining() == 0);
        super.write(filtered, true);
        if (bb != null)
            bb.clear();
    }
}
