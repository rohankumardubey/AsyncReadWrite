package org.example;

import static java.nio.file.StandardOpenOption.READ;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
public class ReadUsingCompletitionHandler {
    public static void main(String[] args) throws Exception{
        long t1 = System.currentTimeMillis();
        Path path = Paths.get("test.txt");
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, READ);
        ReadHandler handler = new ReadHandler();
        int fileSize = (int) afc.size();
        ByteBuffer dataBuffer = ByteBuffer.allocate(fileSize);

        AttachmentWrite attach = new AttachmentWrite();
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;

        afc.read(dataBuffer, 0, attach, handler);

        System.out.println("time taken: "
                + (System.currentTimeMillis() - t1) + " ms");
    }
}
class AttachmentWrite {
    public Path path;
    public ByteBuffer buffer;
    public AsynchronousFileChannel asyncChannel;
}

class ReadHandler implements CompletionHandler<Integer, AttachmentWrite> {
    @Override
    public void completed(Integer result, AttachmentWrite attach) {
        System.out.format("%s bytes read   from  %s%n", result, attach.path);
        System.out.format("Read data is:%n");
        byte[] byteData = attach.buffer.array();
        Charset cs = StandardCharsets.UTF_8;
        String data = new String(byteData, cs);
        System.out.println(data);
        try {
            // Close the channel
            attach.asyncChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable e, AttachmentWrite attach) {
        System.out.format("Read operation  on  %s  file failed."
                + "The  error is: %s%n", attach.path, e.getMessage());
        try {
            // Close the channel
            attach.asyncChannel.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}