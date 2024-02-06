package org.example;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
public class WriteUsingCompetitionHandler {
    public static void main(String[] args) throws Exception {
        long t1 = System.currentTimeMillis();
        Path path = Paths.get("/src/main/resources/test.txt");
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, WRITE,
                CREATE);
        WriteHandler handler = new WriteHandler();
        ByteBuffer dataBuffer = getDataBuffer();
        Attachment attach = new Attachment();
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;

        afc.write(dataBuffer, 0, attach, handler);
        System.out.println("time taken: "
                + (System.currentTimeMillis() - t1) + " ms");
//        System.out.println("Sleeping for 5  seconds...");
//        Thread.sleep(5000);
    }
    public static ByteBuffer getDataBuffer() {
        String lineSeparator = System.lineSeparator();
        String str = "test "+Math.random()+lineSeparator;
        Charset cs = StandardCharsets.UTF_8;
        return ByteBuffer.wrap(str.getBytes(cs));
    }
}
class Attachment {
    public Path path;
    public ByteBuffer buffer;
    public AsynchronousFileChannel asyncChannel;
}

class WriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer result, Attachment attach) {
        System.out.format("%s bytes written  to  %s%n", result,
                attach.path.toAbsolutePath());
        try {
            attach.asyncChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable e, Attachment attach) {
        try {
            attach.asyncChannel.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
