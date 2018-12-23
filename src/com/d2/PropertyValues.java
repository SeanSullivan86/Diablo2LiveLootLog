package com.d2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2.properties.CompositeProperty;
import com.d2.properties.CompositePropertyValue;
import com.d2.properties.ItemProperty;
import com.d2.properties.ItemPropertyDao;
import com.d2.properties.ItemPropertyValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public class PropertyValues {
	private ListMultimap<ItemProperty,ItemPropertyValue> propertyValues;
	private Map<CompositeProperty, CompositePropertyValue> compositeProperties;
	
	public PropertyValues(ListMultimap<ItemProperty,ItemPropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
		this.compositeProperties = new HashMap<>();
		
		for (CompositeProperty compositeProperty : CompositeProperty.values()) {
			boolean hasProp = false;
			for (int id : compositeProperty.getComponentPropertyIds()) {
				if (propertyValues.containsKey(ItemPropertyDao.get().getById(id))) {
					hasProp = true;
				}
			}
			if (hasProp) {
				compositeProperties.put(compositeProperty, compositeProperty.getValueFunc().apply(propertyValues));
			}
		}
	}
	
	public ItemPropertyValue getOnlyValue(int id) {
		return getOnlyValue(ItemPropertyDao.get().getById(id));
	}
	
	public ItemPropertyValue getOnlyValue(ItemProperty property) {
		return Iterables.getOnlyElement(propertyValues.get(property));
	}
	
	public List<ItemPropertyValue> getById(int id) {
		return propertyValues.get(ItemPropertyDao.get().getById(id));
	}
	
	public List<ItemPropertyValue> get(ItemProperty property) {
		return propertyValues.get(property);
	}
	
	@JsonIgnore
	public ListMultimap<ItemProperty,ItemPropertyValue> getProperties() {
		return Multimaps.filterKeys(propertyValues, prop -> ( !CompositeProperty.propertiesCoveredByComposites().contains(prop.getId())));
	}
	
	public Map<CompositeProperty, CompositePropertyValue> getCompositeProperties() {
		return compositeProperties;
	}
	
	@JsonIgnore
	public boolean hasProperty(int id) {
		return propertyValues.containsKey(ItemPropertyDao.get().getById(id));
	}
	
	public List<PropertyAndValue> getOtherProperties() {
		List<PropertyAndValue> vals = new ArrayList<>();
		for (ItemProperty prop : getProperties().keySet()) {
			for (ItemPropertyValue value : propertyValues.get(prop) ) {
				vals.add(new PropertyAndValue(prop, value));
			}
		}
		return vals;
	}
}

class PropertyAndValue {
	private ItemProperty property;
	private ItemPropertyValue value;
	
	public PropertyAndValue(ItemProperty property, ItemPropertyValue value) {
		this.property = property;
		this.value = value;
	}

	public ItemProperty getProperty() {
		return property;
	}

	public ItemPropertyValue getValue() {
		return value;
	}
	
	
}