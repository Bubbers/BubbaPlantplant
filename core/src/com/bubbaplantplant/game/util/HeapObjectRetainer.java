package com.bubbaplantplant.game.util;

import java.util.*;

public class HeapObjectRetainer {

    private static List<Object> objectsToRetain = new ArrayList<>();
    private static Map<String, Object> idObjectsToRetain = new HashMap<>();

    public static void addObjectForever(Object objectToRetain) {
        objectsToRetain.add(objectToRetain);
    }

    public static void addObjectsForever(Object... objectToRetain) {
        objectsToRetain.addAll(Arrays.asList(objectToRetain));
    }

    public static void addRemovableObject(String id, Object objectToRetain) {
        if (idObjectsToRetain.containsKey(id)) {
            throw new IllegalArgumentException("Id '" + id + "' is already taken");
        }
        idObjectsToRetain.put(id, objectToRetain);
    }

    /**
     *
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public static Object removeObject(String id) {
        return idObjectsToRetain.remove(id);
    }

}
