package com.org.gunbbang.util.mapper.context;

import java.util.IdentityHashMap;
import java.util.Map;

public class CycleAvoidingMappingContext {
  private Map<Object, Object> knownInstances = new IdentityHashMap<>();

  public Object getKnownInstance(Object source) {
    return knownInstances.get(source);
  }

  public void storeKnownInstance(Object source, Object destination) {
    knownInstances.put(source, destination);
  }
}
