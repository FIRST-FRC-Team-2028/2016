/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * The preferences class provides a relatively simple way to save important
 * values to the RoboRIO to access the next time the RoboRIO is booted.
 * 
 * <p>This class loads and saves from a file inside the RoboRIO. The user can not
 * access the file directly, but may modify values at specific fields which will
 * then be saved to the file when {@link Preferences#save() save()} is
 * called.</p>
 * 
 * <p>This class is thread safe.</p>
 * 
 * <p>This will also interact with {@link NetworkTable} by creating a table
 * called "Preferences" with all the key-value pairs. To save using
 * {@link NetworkTable}, simply set the boolean at position ~S A V E~ to true.
 * Also, if the value of any variable is " in the {@link NetworkTable}, then
 * that represents non-existence in the {@link Preferences} table</p>
 * 
 * @author Joe Grinstead
 */
@objid ("89ad0b89-8fae-4b49-82f6-4bdeb4ffb2aa")
public class Preferences {
    /**
     * The Preferences table name
     */
    @objid ("e0f09cae-208d-434e-bfba-f7d3dad66105")
    private static final String TABLE_NAME = "Preferences";

    /**
     * The value of the save field
     */
    @objid ("65732757-a245-43fa-9add-fdd2abf865b7")
    private static final String SAVE_FIELD = "~S A V E~";

    /**
     * The file to save to
     */
    @objid ("7f30d3d8-5e3a-49ef-9964-23632332f76d")
    private static final String FILE_NAME = "/home/lvuser/wpilib-preferences.ini";

    /**
     * The characters to put between a field and value
     */
    @objid ("96efeaf5-050a-44fc-8e70-b466b96deace")
    private static final byte[] VALUE_PREFIX = {'=', '\"'};

    /**
     * The characters to put after the value
     */
    @objid ("aa94e268-637d-4c28-835c-adb39fa39b7e")
    private static final byte[] VALUE_SUFFIX = {'\"', '\n'};

    /**
     * The newline character
     */
    @objid ("a0e1e426-dea3-41cf-a539-8d63a57b5ee8")
    private static final byte[] NEW_LINE = {'\n'};

    /**
     * The semaphore for beginning reads and writes to the file
     */
    @objid ("9c4d98fe-70ed-4544-8cc5-21579ddbf9c5")
    private final Object fileLock = new Object();

    /**
     * The semaphore for reading from the table
     */
    @objid ("087e6e58-0c11-4534-b896-0955fd130dba")
    private final Object lock = new Object();

    /**
     * The actual values (String->String)
     */
    @objid ("3d1ac5ad-13a9-4a64-88bb-9dce80475cba")
    private Hashtable values;

    /**
     * The keys in the order they were read from the file
     */
    @objid ("4606ca02-7d7b-45b0-b5ee-24fc69fbb4d6")
    private Vector keys;

    /**
     * The comments that were in the file sorted by which key they appeared over
     * (String->Comment)
     */
    @objid ("bc37161e-a20b-4a6a-897c-ee39a6bb8d18")
    private Hashtable comments;

    /**
     * The singleton instance
     */
    @objid ("57ab2ca2-a3d9-4da1-b842-24d5d08aaf77")
    private static Preferences instance;

    /**
     * The comment at the end of the file
     */
    @objid ("018d33b2-e403-4d34-8d21-17315ec32d92")
    private Comment endComment;

    /**
     * Returns the preferences instance.
     * @return the preferences instance
     */
    @objid ("b83aa46d-19c0-4326-9012-7affaf2fcf29")
    public static synchronized Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    /**
     * Creates a preference class that will automatically read the file in a
     * different thread. Any call to its methods will be blocked until the
     * thread is finished reading.
     */
    @objid ("7eceb44e-dc03-4557-98e4-367331616d36")
    private Preferences() {
        values = new Hashtable();
        keys = new Vector();
        
        // We synchronized on fileLock and then wait
        // for it to know that the reading thread has started
        synchronized (fileLock) {
            new Thread() {
                public void run() {
                    read();
                }
            } .start();
            try {
                fileLock.wait();
            } catch (InterruptedException ex) {
            }
        }
        
        UsageReporting.report(tResourceType.kResourceType_Preferences, 0);
    }

    /**
     * @return a vector of the keys
     */
    @objid ("1ec34541-a1d4-469f-af54-46f6b2f62c5c")
    public Vector getKeys() {
        synchronized (lock) {
            return keys;
        }
    }

    /**
     * Puts the given value into the given key position
     * @throws ImproperPreferenceKeyException if the key contains an illegal
     * character
     * @param key the key
     * @param value the value
     */
    @objid ("bc205b12-ec71-421b-8694-3230eb4c8148")
    private void put(String key, String value) {
        synchronized (lock) {
            if (key == null) {
                throw new NullPointerException();
            }
            ImproperPreferenceKeyException.confirmString(key);
            if (values.put(key, value) == null) {
                keys.addElement(key);
            }
            NetworkTable.getTable(TABLE_NAME).putString(key, value);
        }
    }

    /**
     * Puts the given string into the preferences table.
     * 
     * <p>The value may not have quotation marks, nor may the key have any
     * whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care). at some point after calling this.</p>
     * @throws NullPointerException if value is null
     * @throws IllegalArgumentException if value contains a quotation mark
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("fba97bbb-1e47-43e5-8650-ae8e3de58aad")
    public void putString(String key, String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (value.indexOf('"') != -1) {
            throw new IllegalArgumentException("Can not put string:" + value + " because it contains quotation marks");
        }
        put(key, value);
    }

    /**
     * Puts the given int into the preferences table.
     * 
     * <p>The key may not have any whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care) at some point after calling this.</p>
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("019c669f-838a-44f1-aed7-e9a99dec146b")
    public void putInt(String key, int value) {
        put(key, String.valueOf(value));
    }

    /**
     * Puts the given double into the preferences table.
     * 
     * <p>The key may not have any whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care) at some point after calling this.</p>
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("4e028112-ef18-4b79-b760-4115f471ef26")
    public void putDouble(String key, double value) {
        put(key, String.valueOf(value));
    }

    /**
     * Puts the given float into the preferences table.
     * 
     * <p>The key may not have any whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care) at some point after calling this.</p>
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("8311b907-8ccd-4dff-a1f2-a62c63d075f4")
    public void putFloat(String key, float value) {
        put(key, String.valueOf(value));
    }

    /**
     * Puts the given boolean into the preferences table.
     * 
     * <p>The key may not have any whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care) at some point after calling this.</p>
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("77a557ab-30c1-44fa-a3d4-47aa5137a216")
    public void putBoolean(String key, boolean value) {
        put(key, String.valueOf(value));
    }

    /**
     * Puts the given long into the preferences table.
     * 
     * <p>The key may not have any whitespace nor an equals sign</p>
     * 
     * <p>This will <b>NOT</b> save the value to memory between power cycles, to
     * do that you must call {@link Preferences#save() save()} (which must be
     * used with care) at some point after calling this.</p>
     * @throws ImproperPreferenceKeyException if the key contains any whitespace
     * or an equals sign
     * @param key the key
     * @param value the value
     */
    @objid ("31d28d41-540a-4768-b69e-b4898509140e")
    public void putLong(String key, long value) {
        put(key, String.valueOf(value));
    }

    /**
     * Returns the value at the given key.
     * @throws NullPointerException if the key is null
     * @param key the key
     * @return the value (or null if none exists)
     */
    @objid ("924ae4ba-a3fc-4683-9a74-745210bf4cb3")
    private String get(String key) {
        synchronized (lock) {
            if (key == null) {
                throw new NullPointerException();
            }
            return (String) values.get(key);
        }
    }

    /**
     * Returns whether or not there is a key with the given name.
     * @throws NullPointerException if key is null
     * @param key the key
     * @return if there is a value at the given key
     */
    @objid ("d9010aea-0137-4d81-ba1f-758722f8652d")
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    /**
     * Remove a preference
     * @throws NullPointerException if key is null
     * @param key the key
     */
    @objid ("8c129097-5ba5-4fc4-a8fc-574ca2d06763")
    public void remove(String key) {
        synchronized (lock) {
            if (key == null) {
                throw new NullPointerException();
            }
            values.remove(key);
            keys.removeElement(key);
        }
    }

    /**
     * Returns the string at the given key. If this table does not have a value
     * for that position, then the given backup value will be returned.
     * @throws NullPointerException if the key is null
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("d122c469-452e-4633-821c-3eade3a0bf89")
    public String getString(String key, String backup) {
        String value = get(key);
        return value == null ? backup : value;
    }

    /**
     * Returns the int at the given key. If this table does not have a value for
     * that position, then the given backup value will be returned.
     * @throws IncompatibleTypeException if the value in the table can not be
     * converted to an int
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("80f4e4e3-f1cc-4217-82de-1e8f6b5fdf8f")
    public int getInt(String key, int backup) {
        String value = get(key);
        if (value == null) {
            return backup;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IncompatibleTypeException(value, "int");
            }
        }
    }

    /**
     * Returns the double at the given key. If this table does not have a value
     * for that position, then the given backup value will be returned.
     * @throws IncompatibleTypeException if the value in the table can not be
     * converted to an double
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("f2336d3a-b568-4efc-9c91-f9eeca925b6d")
    public double getDouble(String key, double backup) {
        String value = get(key);
        if (value == null) {
            return backup;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IncompatibleTypeException(value, "double");
            }
        }
    }

    /**
     * Returns the boolean at the given key. If this table does not have a value
     * for that position, then the given backup value will be returned.
     * @throws IncompatibleTypeException if the value in the table can not be
     * converted to a boolean
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("ac5bd7c5-1973-454e-b3d5-373a40a77eac")
    public boolean getBoolean(String key, boolean backup) {
        String value = get(key);
        if (value == null) {
            return backup;
        } else {
            if (value.equalsIgnoreCase("true")) {
                return true;
            } else if (value.equalsIgnoreCase("false")) {
                return false;
            } else {
                throw new IncompatibleTypeException(value, "boolean");
            }
        }
    }

    /**
     * Returns the float at the given key. If this table does not have a value
     * for that position, then the given backup value will be returned.
     * @throws IncompatibleTypeException if the value in the table can not be
     * converted to a float
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("776a4fd3-016e-4a31-bed9-682ae03bed38")
    public float getFloat(String key, float backup) {
        String value = get(key);
        if (value == null) {
            return backup;
        } else {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                throw new IncompatibleTypeException(value, "float");
            }
        }
    }

    /**
     * Returns the long at the given key. If this table does not have a value
     * for that position, then the given backup value will be returned.
     * @throws IncompatibleTypeException if the value in the table can not be
     * converted to a long
     * @param key the key
     * @param backup the value to return if none exists in the table
     * @return either the value in the table, or the backup
     */
    @objid ("bee2e05b-cf48-401b-bf84-54c007acd92b")
    public long getLong(String key, long backup) {
        String value = get(key);
        if (value == null) {
            put(key, String.valueOf(backup));
            return backup;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new IncompatibleTypeException(value, "long");
            }
        }
    }

    /**
     * Saves the preferences to a file on the RoboRIO.
     * 
     * <p>This should <b>NOT</b> be called often. Too many writes can damage the
     * RoboRIO's flash memory. While it is ok to save once or twice a match, this
     * should never be called every run of
     * {@link IterativeRobot#teleopPeriodic()}.</p>
     * 
     * <p>The actual writing of the file is done in a separate thread. However,
     * any call to a get or put method will wait until the table is fully saved
     * before continuing.</p>
     */
    @objid ("0c4801b2-36ba-491f-b4f1-d8f6f357fc96")
    public void save() {
        synchronized (fileLock) {
            new Thread() {
                public void run() {
                    write();
                }
            } .start();
            try {
                fileLock.wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Internal method that actually writes the table to a file. This is called
     * in its own thread when {@link Preferences#save() save()} is called.
     */
    @objid ("a2734a1d-e8e5-4dfd-859d-f8d5a9ea4e31")
    private void write() {
        synchronized (lock) {
            synchronized (fileLock) {
                fileLock.notifyAll();
            }
        
            File file = null;
            FileOutputStream output = null;
            try {
                file = new File(FILE_NAME);
        
                if (file.exists())
                    file.delete();
        
                file.createNewFile();
        
                output = new FileOutputStream(file);
        
                output.write("[Preferences]\n".getBytes());
        
                for (int i = 0; i < keys.size(); i++) {
                    String key = (String) keys.elementAt(i);
                    String value = (String) values.get(key);
        
                    if (comments != null) {
                        Comment comment = (Comment) comments.get(key);
                        if (comment != null) {
                            comment.write(output);
                        }
                    }
        
                    output.write(key.getBytes());
                    output.write(VALUE_PREFIX);
                    output.write(value.getBytes());
                    output.write(VALUE_SUFFIX);
                }
        
                if (endComment != null) {
                    endComment.write(output);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                    }
                }
                NetworkTable.getTable(TABLE_NAME).putBoolean(SAVE_FIELD, false);
            }
        }
    }

    /**
     * The internal method to read from a file. This will be called in its own
     * thread when the preferences singleton is first created.
     */
    @objid ("9948ea09-d297-4db0-aff0-3332984f7f60")
    private void read() {
        class EndOfStreamException extends Exception {
        }
        
        class Reader {
        
            InputStream stream;
        
            Reader(InputStream stream) {
                this.stream = stream;
            }
        
            public char read() throws IOException, EndOfStreamException {
                int input = stream.read();
                if (input == -1) {
                    throw new EndOfStreamException();
                } else {
                    // Check for carriage returns
                    return input == '\r' ? '\n' : (char) input;
                }
            }
        
            char readWithoutWhitespace() throws IOException, EndOfStreamException {
                while (true) {
                    char value = read();
                    switch (value) {
                    case ' ':
                    case '\t':
                        continue;
                    default:
                        return value;
                    }
                }
            }
        }
        
        synchronized (lock) {
            synchronized (fileLock) {
                fileLock.notifyAll();
            }
        
            Comment comment = null;
        
        
        
            File file = null;
            FileInputStream input = null;
            try {
                file = new File(FILE_NAME);
        
                if (file.exists()) {
                    input = new FileInputStream(file);
                    Reader reader = new Reader(input);
        
                    StringBuffer buffer;
        
                    while (true) {
                        char value = reader.readWithoutWhitespace();
        
                        if (value == '\n' || value == ';') {
                            if (comment == null) {
                                comment = new Comment();
                            }
        
                            if (value == '\n') {
                                comment.addBytes(NEW_LINE);
                            } else {
                                buffer = new StringBuffer(30);
                                for (; value != '\n'; value = reader.read()) {
                                    buffer.append(value);
                                }
                                buffer.append('\n');
                                comment.addBytes(buffer.toString().getBytes());
                            }
                        } else if (value == '[') {
                            // Find the end of the section and the new line after it and throw it away
                            while (reader.read() !=']');
                            while (reader.read() != '\n');
                        } else {
                            buffer = new StringBuffer(30);
                            for (; value != '='; value = reader.readWithoutWhitespace()) {
                                buffer.append(value);
                            }
                            String name = buffer.toString();
                            buffer = new StringBuffer(30);
        
                            boolean shouldBreak = false;
        
                            value = reader.readWithoutWhitespace();
                            if (value == '"') {
                                for (value = reader.read(); value != '"'; value = reader.read()) {
                                    buffer.append(value);
                                }
                                // Clear the line
                                while (reader.read() != '\n');
                            } else {
                                try {
                                    for (; value != '\n'; value = reader.readWithoutWhitespace()) {
                                        buffer.append(value);
                                    }
                                } catch (EndOfStreamException e) {
                                    shouldBreak = true;
                                }
                            }
        
                            String result = buffer.toString();
        
                            keys.addElement(name);
                            values.put(name, result);
                            NetworkTable.getTable(TABLE_NAME).putString(name, result);
        
                            if (comment != null) {
                                if (comments == null) {
                                    comments = new Hashtable();
                                }
                                comments.put(name, comment);
                                comment = null;
                            }
        
        
                            if (shouldBreak) {
                                break;
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (EndOfStreamException ex) {
                System.out.println("Done Reading");
            }
        
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                }
            }
        
            if (comment != null) {
                endComment = comment;
            }
        }
        
        NetworkTable.getTable(TABLE_NAME).putBoolean(SAVE_FIELD, false);
        // TODO: Verify that this works even though it changes with subtables.
        //       Should work since preferences shouldn't have subtables.
        NetworkTable.getTable(TABLE_NAME).addTableListener(new ITableListener() {
            public void valueChanged(ITable source, String key, Object value, boolean isNew) {
                if (key.equals(SAVE_FIELD)) {
                    if (((Boolean) value).booleanValue()) {
                        save();
                    }
                } else {
                    synchronized (lock) {
                        if (!ImproperPreferenceKeyException.isAcceptable(key) || value.toString().indexOf('"') != -1) {
                            if (values.contains(key) || keys.contains(key)) {
                                values.remove(key);
                                keys.removeElement(key);
                                NetworkTable.getTable(TABLE_NAME).putString(key, "\"");
                            }
                        } else {
                            if (values.put(key, value.toString()) == null) {
                                keys.addElement(key);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * A class representing some comment lines in the ini file. This is used so
     * that if a programmer ever directly modifies the ini file, then his/her
     * comments will still be there after {@link Preferences#save() save()} is
     * called.
     */
    @objid ("0cd8cf17-ba3d-4469-9a0a-00661c971460")
    private static class Comment {
        /**
         * A vector of byte arrays. Each array represents a line to write
         */
        @objid ("ecbc9314-ed03-48d3-9c5c-afab6c93f249")
        private Vector bytes = new Vector();

        /**
         * Appends the given bytes to the comment.
         * @param bytes the bytes to add
         */
        @objid ("6b23203f-2ede-443b-a61f-9caea5697e1f")
        private void addBytes(byte[] bytes) {
            this.bytes.addElement(bytes);
        }

        /**
         * Writes this comment to the given stream
         * @throws IOException if the stream has a problem
         * @param stream the stream to write to
         */
        @objid ("3fd42b49-321f-43dd-a74e-f555b2e7013d")
        private void write(OutputStream stream) throws IOException {
            for (int i = 0; i < bytes.size(); i++) {
                stream.write((byte[]) bytes.elementAt(i));
            }
        }

    }

    /**
     * This exception is thrown if the a value requested cannot be converted to
     * the requested type.
     */
    @objid ("d0cb7efa-8b8b-4587-8911-b28b0c4f895e")
    public static class IncompatibleTypeException extends RuntimeException {
        /**
         * Creates an exception with a description based on the input
         * @param value the value that can not be converted
         * @param type the type that the value can not be converted to
         */
        @objid ("20200090-30a6-4503-93f2-4bb9540db9f7")
        public IncompatibleTypeException(String value, String type) {
            super("Cannot convert \"" + value + "\" into " + type);
        }

    }

    /**
     * Should be thrown if a string can not be used as a key in the preferences
     * file. This happens if the string contains a new line, a space, a tab, or
     * an equals sign.
     */
    @objid ("5d704ac5-d11d-47ce-9cc7-489701641985")
    public static class ImproperPreferenceKeyException extends RuntimeException {
        /**
         * Instantiates an exception with a descriptive message based on the
         * input.
         * @param value the illegal key
         * @param letter the specific character that made it illegal
         */
        @objid ("6fd7bd58-1427-4ec2-b975-d345ff12953f")
        public ImproperPreferenceKeyException(String value, char letter) {
            super("Preference key \""
                  + value + "\" is not allowed to contain letter with ASCII code:" + (byte) letter);
        }

        /**
         * Tests if the given string is ok to use as a key in the preference
         * table. If not, then a {@link ImproperPreferenceKeyException} will be
         * thrown.
         * @param value the value to test
         */
        @objid ("0ade25d8-1401-41c6-a5a4-da7bba4b6715")
        public static void confirmString(String value) {
            for (int i = 0; i < value.length(); i++) {
                char letter = value.charAt(i);
                switch (letter) {
                case '=':
                case '\n':
                case '\r':
                case ' ':
                case '\t':
                case '[':
                case ']':
                    throw new ImproperPreferenceKeyException(value, letter);
                }
            }
        }

        /**
         * Returns whether or not the given string is ok to use in the
         * preference table.
         * @param value the string to check
         * @return true if the given string is ok to use in the preference table
         */
        @objid ("5c7fd49f-ea3d-4fe5-bf46-30a7a9308889")
        public static boolean isAcceptable(String value) {
            for (int i = 0; i < value.length(); i++) {
                char letter = value.charAt(i);
                switch (letter) {
                case '=':
                case '\n':
                case '\r':
                case ' ':
                case '\t':
                case '[':
                case ']':
                    return false;
                }
            }
            return true;
        }

    }

}
