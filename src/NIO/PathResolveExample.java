package NIO;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResolveExample {
    public static void main(String[] args) {
        Path basePath = Path.of("/home/samwan");
        Path relativePath = Paths.get("Downloads");
        Path resolvedPath = basePath.resolve(relativePath);
        System.out.println(resolvedPath);

        PathResolveExample pathResolveExample = new PathResolveExample();
        pathResolveExample.test(new String[] { "a", "b" });
        pathResolveExample.test(new int[]{1, 2, 3});
    }

    void test(Integer... args) {
        System.out.println("Integer args");
    }

    void test(int... args) {
        System.out.println("int args");
    }

    void test(Object args) {
        System.out.println("Object args");
    }
}
