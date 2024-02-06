package org.example;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class WriteUsingFutureObject {
    public static ByteBuffer getDataBuffer() {
        String lineSeparator = System.lineSeparator();

        String str = "test" +
                lineSeparator;
        Charset cs = StandardCharsets.UTF_8;

        return ByteBuffer.wrap(str.getBytes(cs));
    }

    public static void main(String[] args) throws Exception {
        long t1 = System.currentTimeMillis();
        Path path = Paths.get("test.txt");

        try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(path,
                WRITE, CREATE)) {
            ByteBuffer dataBuffer = getDataBuffer();
            Future<Integer> result = afc.write(dataBuffer, 0);
//            while (!result.isDone()) {
//                System.out.println("Sleeping for 2  seconds...");
//                Thread.sleep(2000);
//            }
            int writtenBytes = result.get();
            System.out.format("%s bytes written  to  %s%n", writtenBytes,
                    path.toAbsolutePath());

            System.out.println("time taken: "
                    + (System.currentTimeMillis() - t1) + " ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}