package de.knoxter.internal.reflectutils.models;

import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PrimClsContainer {
    private byte primByte;
    private short primShort;
    private int primInt;
    private long primLong;
    private float primFloat;
    private double primDouble;
    
    private byte[] primByteArray;
    private short[] primShortArray;
    private int[] primIntArray;
    private long[] primLongArray;
    private float[] primFloatArray;
    private double[] primDoubleArray;

    private Byte clsByte;
    private Short clsShort;
    private Integer clsInt;
    private Long clsLong;
    private Float clsFloat;
    private Double clsDouble;

    private List<Byte> clsByteList;
    private List<Short> clsShortList;
    private List<Integer> clsIntList;
    private List<Long> clsLongList;
    private List<Float> clsFloatList;
    private List<Double> clsDoubleList;

    public static PrimClsContainer empty() {
        return new PrimClsContainer();
    }

    public static PrimClsContainer filled() {
        PrimClsContainer container = new PrimClsContainer();
        container.setTestValues();
        return container;
    }

    public void setTestValues() {
        primByte = 10;
        primShort = 20;
        primInt = 30;
        primLong = 40;
        primFloat = 50.5f;
        primDouble = 60.6;
        
        primByteArray = new byte[]{ 10, 11, 12 };
        primShortArray = new short[]{ 20, 21, 22 };
        primIntArray = new int[]{ 30, 31, 32 };
        primLongArray = new long[]{ 40, 41, 42 };
        primFloatArray = new float[]{ 50.5f, 51.51f, 52.52f };
        primDoubleArray = new double[]{ 60.6, 61.61, 62.62 };

        clsByte = 110;
        clsShort = 120;
        clsInt = 130;
        clsLong = 140L;
        clsFloat = 150.15f;
        clsDouble = 160.16;

        clsByteList = Arrays.asList(new Byte[]{ 10, 11, 12 });
        clsShortList = Arrays.asList(new Short[]{ 20, 21, 22 });
        clsIntList = Arrays.asList(new Integer[]{ 30, 31, 32 });
        clsLongList = Arrays.asList(new Long[]{ 40L, 41L, 42L });
        clsFloatList = Arrays.asList(new Float[]{ 50.5f, 51.51f, 52.52f });
        clsDoubleList = Arrays.asList(new Double[]{ 60.6, 61.61, 62.62 });
    }
}
