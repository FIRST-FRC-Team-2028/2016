package edu.wpi.first.wpilibj.hal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

//
// base class for all JNI wrappers
//
@objid ("79503753-548f-47ba-b77f-e527100a0c9f")
public class JNIWrapper {
    @objid ("46b8be4c-0f4f-463d-87d7-fef2f7a856f1")
     static boolean libraryLoaded = false;

    @objid ("e5359a96-4141-434e-a24b-092cc4d27e5d")
     static File jniLibrary = null;

    @objid ("2b1eb96a-be36-48ff-b71b-5870a1d14458")
    public static native ByteBuffer getPortWithModule(byte module, byte pin);

    @objid ("2ff5b95c-b0df-4c16-a0cc-d28fbae33111")
    public static native ByteBuffer getPort(byte pin);


static
    {
        try
        {
            if( !libraryLoaded )
            {
                // create temporary file
                jniLibrary = File.createTempFile("libwpilibJavaJNI", ".so");
                // flag for delete on exit
                jniLibrary.deleteOnExit();

                byte [] buffer = new byte[1024];

                int readBytes;

                InputStream is = JNIWrapper.class.getResourceAsStream("/linux-arm/libwpilibJavaJNI.so");

                OutputStream os = new FileOutputStream(jniLibrary);

                try
                {
                    while((readBytes = is.read(buffer)) != -1 )
                    {
                        os.write(buffer, 0, readBytes);
                    }

                }
                finally
                {
                    os.close();
                    is.close();
                }


                libraryLoaded = true;
            }

            System.load(jniLibrary.getAbsolutePath());
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
