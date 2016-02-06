/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.smartdashboard;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 * The {@link SmartDashboard} class is the bridge between robot programs and the SmartDashboard on the
 * laptop.
 * 
 * <p>When a value is put into the SmartDashboard here, it pops up on the SmartDashboard on the laptop.
 * Users can put values into and get values from the SmartDashboard</p>
 * 
 * @author Joe Grinstead
 */
@objid ("54c956ae-f340-47c8-84c0-a8ca6f3a1d65")
public class SmartDashboard {
    /**
     * The {@link NetworkTable} used by {@link SmartDashboard}
     */
    @objid ("a9b6a43c-5909-40f9-8482-af3e0f7808dd")
    private static final NetworkTable table = NetworkTable.getTable("SmartDashboard");

    /**
     * A table linking tables in the SmartDashboard to the {@link SmartDashboardData} objects
     * they came from.
     */
    @objid ("6eeb63bf-c7f4-40e1-8ce5-7d99b24a0f31")
    private static final Hashtable tablesToData = new Hashtable();

    /**
     * Maps the specified key to the specified value in this table.
     * The key can not be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @throws IllegalArgumentException if key is null
     * @param key the key
     * @param data the value
     */
    @objid ("6f37194f-94d8-4507-8dc3-87b9c3972a5d")
    public static void putData(String key, Sendable data) {
        ITable dataTable = table.getSubTable(key);
        dataTable.putString("~TYPE~", data.getSmartDashboardType());
        data.initTable(dataTable);
        tablesToData.put(data, key);
    }

//TODO should we reimplement NamedSendable?
    /**
     * Maps the specified key (where the key is the name of the {@link NamedSendable} SmartDashboardNamedData
     * to the specified value in this table.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @throws IllegalArgumentException if key is null
     * @param value the value
     */
    @objid ("b85905b7-b544-4cb9-a596-47302c84c449")
    public static void putData(NamedSendable value) {
        putData(value.getName(), value);
    }

    /**
     * Returns the value at the specified key.
     * @throws NetworkTableKeyNotDefined if there is no value mapped to by the key
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("eb668712-9eea-4640-86ab-1bb6a7dd0c57")
    public static Sendable getData(String key) {
        ITable subtable = table.getSubTable(key);
        Object data = tablesToData.get(subtable);
        if (data == null) {
            throw new IllegalArgumentException("SmartDashboard data does not exist: " + key);
        } else {
            return (Sendable) data;
        }
    }

    /**
     * Maps the specified key to the specified value in this table.
     * The key can not be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @throws IllegalArgumentException if key is null
     * @param key the key
     * @param value the value
     */
    @objid ("be368d11-fc5e-4f0a-a6c3-f94534dc7177")
    public static void putBoolean(String key, boolean value) {
        table.putBoolean(key, value);
    }

    /**
     * Returns the value at the specified key.
     * @throws NetworkTableKeyNotDefined if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a boolean
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("0ca00f7f-4177-4064-a7b4-92a16609b3a4")
    public static boolean getBoolean(String key) throws TableKeyNotDefinedException {
        return table.getBoolean(key);
    }

    /**
     * Returns the value at the specified key.
     * @throws IllegalArgumentException if the value mapped to by the key is not a boolean
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @param defaultValue returned if the key doesn't exist
     * @return the value
     */
    @objid ("f9ce6b8f-64d1-498e-af1e-fe270417a66b")
    public static boolean getBoolean(String key, boolean defaultValue) {
        return table.getBoolean(key, defaultValue);
    }

    /**
     * Maps the specified key to the specified value in this table.
     * The key can not be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @throws IllegalArgumentException if key is null
     * @param key the key
     * @param value the value
     */
    @objid ("cc7dcaad-7125-4226-8fd1-4c82736500a3")
    public static void putNumber(String key, double value) {
        table.putNumber(key, value);
    }

    /**
     * Returns the value at the specified key.
     * @throws TableKeyNotDefinedException if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a double
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("7907e7f1-07bc-44ab-9ce9-d2c2957f8fe2")
    public static double getNumber(String key) throws TableKeyNotDefinedException {
        return table.getNumber(key);
    }

    /**
     * Returns the value at the specified key.
     * @throws NetworkTableKeyNotDefined if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a double
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @param defaultValue the value returned if the key is undefined
     * @return the value
     */
    @objid ("76c8564b-541b-4739-a618-e8e67d5fcf85")
    public static double getNumber(String key, double defaultValue) {
        return table.getNumber(key, defaultValue);
    }

    /**
     * Maps the specified key to the specified value in this table.
     * Neither the key nor the value can be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @throws IllegalArgumentException if key or value is null
     * @param key the key
     * @param value the value
     */
    @objid ("4f718d48-904c-4d0d-9616-9427f0d88432")
    public static void putString(String key, String value) {
        table.putString(key, value);
    }

    /**
     * Returns the value at the specified key.
     * @throws NetworkTableKeyNotDefined if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a string
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("ff42fd21-8b64-4fc1-9768-bf9bd32986dd")
    public static String getString(String key) throws TableKeyNotDefinedException {
        return table.getString(key);
    }

    /**
     * Returns the value at the specified key.
     * @throws NetworkTableKeyNotDefined if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a string
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @param defaultValue The value returned if the key is undefined
     * @return the value
     */
    @objid ("ba84a286-4b9a-410d-bcc1-26f3ca3df2c9")
    public static String getString(String key, String defaultValue) {
        return table.getString(key, defaultValue);
    }

/*
     * Deprecated Methods
     */
    /**
     * Maps the specified key to the specified value in this table.
     * 
     * The key can not be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @deprecated Use {@link #putNumber(java.lang.String, double) putNumber method} instead
     * @throws IllegalArgumentException if key is null
     * @param key the key
     * @param value the value
     */
    @objid ("bf912e09-531b-4138-8572-e82423ab3852")
    public static void putInt(String key, int value) {
        table.putNumber(key, value);
    }

    /**
     * Returns the value at the specified key.
     * @deprecated Use {@link #getNumber(java.lang.String) getNumber} instead
     * @throws TableKeyNotDefinedException if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not an int
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("e0c1c398-7dca-486a-bcd2-7a2aa4d75315")
    public static int getInt(String key) throws TableKeyNotDefinedException {
        return (int) table.getNumber(key);
    }

    /**
     * Returns the value at the specified key.
     * @deprecated Use {@link #getNumber(java.lang.String, double) getNumber} instead
     * @throws TableKeyNotDefinedException if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not an int
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @param defaultValue the value returned if the key is undefined
     * @return the value
     */
    @objid ("a9ddc915-79b8-488c-a2e5-70f2f9a6ca72")
    public static int getInt(String key, int defaultValue) throws TableKeyNotDefinedException {
        try {
            return (int) table.getNumber(key);
        } catch (NoSuchElementException ex) {
            return defaultValue;
        }
    }

    /**
     * Maps the specified key to the specified value in this table.
     * 
     * The key can not be null.
     * The value can be retrieved by calling the get method with a key that is equal to the original key.
     * @deprecated Use{@link #putNumber(java.lang.String, double) putNumber} instead
     * @throws IllegalArgumentException if key is null
     * @param key the key
     * @param value the value
     */
    @objid ("d7243964-77ad-411e-984a-87f1dd4fb4ab")
    public static void putDouble(String key, double value) {
        table.putNumber(key, value);
    }

    /**
     * Returns the value at the specified key.
     * @deprecated Use {@link #getNumber(java.lang.String) getNumber} instead
     * @throws TableKeyNotDefinedException if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a double
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @return the value
     */
    @objid ("95110879-632a-4391-b80c-7a2f540385f8")
    public static double getDouble(String key) throws TableKeyNotDefinedException {
        return table.getNumber(key);
    }

    /**
     * Returns the value at the specified key.
     * @deprecated Use {@link #getNumber(java.lang.String, double) getNumber} instead.
     * @throws TableKeyNotDefinedException if there is no value mapped to by the key
     * @throws IllegalArgumentException if the value mapped to by the key is not a double
     * @throws IllegalArgumentException if the key is null
     * @param key the key
     * @param defaultValue the value returned if the key is undefined
     * @return the value
     */
    @objid ("fe75e429-bed3-4e87-a084-ebbe8424aa75")
    public static double getDouble(String key, double defaultValue) {
        return table.getNumber(key, defaultValue);
    }


static {
        HLUsageReporting.reportSmartDashboard();
    }
}
