package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

import static java.nio.file.StandardOpenOption.READ;

public class ReadUsingFutureObject {
    public static void main(String[] args) throws Exception {

        long t1 = System.currentTimeMillis();
        Path path = Paths.get("test.txt");

        try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, READ)) {
            int fileSize = (int) afc.size();
            ByteBuffer dataBuffer = ByteBuffer.allocate(fileSize);

            Future<Integer> result = afc.read(dataBuffer, 0);
            int readBytes = result.get();

            System.out.format("%s bytes read   from  %s%n", readBytes, path);
            System.out.format("Read data is:%n");

            byte[] byteData = dataBuffer.array();
            Charset cs = StandardCharsets.UTF_8;
            String data = new String(byteData, cs);

            System.out.println(data);
            System.out.println("time taken: "
                    + (System.currentTimeMillis() - t1) + " ms");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}