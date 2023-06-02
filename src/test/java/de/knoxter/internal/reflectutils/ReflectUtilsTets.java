package de.knoxter.internal.reflectutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.knoxter.internal.reflectutils.models.PrimClsContainer;

public class ReflectUtilsTets {

    private List<String> getPrimClsContainerPropertNames() {
        return Arrays.asList(new String[]{
            "primByte", "primShort", "primInt", "primLong", "primFloat", "primDouble", 
            "primByteArray", "primShortArray", "primIntArray", "primLongArray", "primFloatArray", "primDoubleArray", 
            "clsByte", "clsShort", "clsInt", "clsLong", "clsFloat", "clsDouble", 
            "clsByteList", "clsShortList", "clsIntList", "clsLongList", "clsFloatList", "clsDoubleList"
        });
    }

    @Test
    public void getMethodNameMap() {
        Map<String, Method> methodNameMap = ReflectUtils.getMethodNameMap(PrimClsContainer.class, "get");
        
        assertNotNull(methodNameMap);
        List<String> expectedGetters = getPrimClsContainerPropertNames();
        assertEquals(expectedGetters.size(), methodNameMap.size());
        for (String expectedGetter : expectedGetters) {
            assertTrue(methodNameMap.containsKey(expectedGetter));
        }
    }
}
