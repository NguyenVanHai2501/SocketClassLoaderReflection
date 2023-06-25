package classLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomClassLoader extends ClassLoader {

    @Override
    public Class findClass(String name) throws ClassFormatError {
        byte[] b = new byte[0];
        try {
            b = loadClassFromFile(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        inputStream.close();
        return buffer;
    }
}
