package NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class NIO2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        final var start = Paths.get("/Users/samwan/Downloads");
        final var dir = start;
        Path path = Path.of("src/NIO/NIO2.java");
        System.out.println(path.getName(0));
        path.forEach(System.out::println);
        Path path1 = Paths.get("/Users/samwan/Downloads/ANGELE_.pdf");
        System.out.println(path1.toAbsolutePath());

        Stream<String> ref = Files.lines(dir);
        //ref.forEach(System.out::println);

        Stream<Path> ref2 = Files.list(dir);
        ref2.forEach(System.out::println);

        DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{pdf,txt}");
        for (Path entry : stream) {
            System.out.println(entry.getFileName());
        }

        AsynchronousFileChannel channel = AsynchronousFileChannel.open(path1, StandardOpenOption.READ);
        /**
         * A ByteBuffer of size 1024 bytes is allocated to hold the data read from the file
         */
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        /**
         * The read method is called on the AsynchronousFileChannel to read data into the buffer starting at position 0. This method returns a Future<Integer> which can be used to check the status of the read operation and retrieve the result once it's complete.
         */
        Future<Integer> result = channel.read(buffer, 0);
        int bytesRead = result.get();
        /**
         * The flip method is called on the buffer to prepare it for reading by setting the limit to the current position and the position to zero.
         *  Why flip() method is used in Java NIO?
         * The flip method on a ByteBuffer is used to prepare the buffer for reading after it has been written to. When you write data to a buffer, the position increases with each write operation. The flip method sets the limit to the current position and then resets the position to zero. This allows you to read the data from the beginning of the buffer up to the limit, which is the amount of data that was written.
         * Writing to the Buffer: When you write data to the buffer, the position moves forward.
         * Flipping the Buffer: The flip method sets the limit to the current position and resets the position to zero.
         * Reading from the Buffer: You can now read the data from the buffer starting from the beginning up to the limit.
         */
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }

        buffer.clear();
        channel.close();

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directoryToWatch = start;
        directoryToWatch.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        try (Stream<Path> paths = Files.walk(start)) {
            System.out.println("Files with two x-ters .pdf extension:");
            paths
                    //.filter(Files::isRegularFile)
                    //.filter(p -> "??.pdf".matches(p.getFileName().toString()))
                    .filter(p -> p.getFileName().toString().matches("\\?.pdf"))
                    .forEach(System.out::println);
        }

        BufferedReader bufferedReader = new BufferedReader(Files.newBufferedReader(Paths.get("/Users/samwan/Documents/file.txt")));
        if (bufferedReader.markSupported()) {
            bufferedReader.mark(100);
            System.out.println((char) bufferedReader.read());
            System.out.println((char) bufferedReader.read());
            bufferedReader.reset();
            System.out.println((char) bufferedReader.read());
        } else {
            System.out.println("mark/reset not supported");
        }

        // Asynchronously read file
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(directoryToWatch, StandardOpenOption.READ);
        ByteBuffer buffer1 = ByteBuffer.allocate(1024);

        fileChannel.read(buffer1, 0, buffer1, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("Read " + result + " bytes");
                attachment.flip();
                while (attachment.hasRemaining()) {
                    System.out.print((char) attachment.get());
                }
                attachment.clear();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Read failed: " + exc.getMessage());
            }
        });

        // Poll for watch service events
        while (true) {
            WatchKey key = watchService.poll(1, TimeUnit.SECONDS);
            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind  = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedPath = (Path) event.context();
                        System.out.println("File modified: " + modifiedPath);
                    }
                    System.out.println("Event kind: " + event.kind() + ". File affected: " + event.context());
                }
                key.reset();
            }
        }
    }
}