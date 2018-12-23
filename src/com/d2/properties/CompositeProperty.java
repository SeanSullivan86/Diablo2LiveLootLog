package com.d2.properties;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;

public enum CompositeProperty {
	POISON_DAMAGE(ImmutableSet.of(57,58,59), properties -> {
		return new PoisonDamage(
				getPropertyValue(properties, 57).getValue(),
				getPropertyValue(properties, 58).getValue(),
				getPropertyValue(properties, 59).getValue());
	}),
	
	FIRE_DAMAGE(ImmutableSet.of(48,49), properties -> {
		return new FireDamage(
				getPropertyValue(properties, 48).getValue(),
				getPropertyValue(properties, 49).getValue());
	}),
	
	LIGHTNING_DAMAGE(ImmutableSet.of(50,51), properties -> {
		return new LightningDamage(
				getPropertyValue(properties, 50).getValue(),
				getPropertyValue(properties, 51).getValue());
	}),
	
	COLD_DAMAGE(ImmutableSet.of(54,55,56), properties -> {
		return new ColdDamage(
				getPropertyValue(properties, 54).getValue(),
				getPropertyValue(properties, 55).getValue(),
				getPropertyValue(properties, 56).getValue());
	}),
	
	DURABILITY(ImmutableSet.of(72,73), properties -> {
		return new Durability(
				getPropertyValue(properties, 72).getValue(),
				getPropertyValue(properties, 73).getValue());
	}),
		
	DAMAGE(ImmutableSet.of(21,22,23,24,159,160), properties -> new Damage(
				getPropertyOptional(properties, 21),
				getPropertyOptional(properties, 22),
				getPropertyOptional(properties, 23),
				getPropertyOptional(properties, 24),
				getPropertyOptional(properties, 159),
				getPropertyOptional(properties, 160))
	);
	
	static ItemPropertyValue getPropertyValue(ListMultimap<ItemProperty,ItemPropertyValue> properties, int id) {
		List<ItemPropertyValue> values = properties.get(ItemPropertyDao.get().getById(id));
		if (values == null || values.isEmpty()) {
			throw new RuntimeException();
		}
		return Iterables.getOnlyElement(values);
	}
	
	static Optional<ItemPropertyValue> getPropertyOptional(ListMultimap<ItemProperty,ItemPropertyValue> properties, int id) {
		List<ItemPropertyValue> values = properties.get(ItemPropertyDao.get().getById(id));
		if (values == null || values.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(Iterables.getOnlyElement(values));
	}
	
	private Set<Integer> componentPropertyIds;
	private Function<ListMultimap<ItemProperty,ItemPropertyValue>, CompositePropertyValue> valueFunc;
	
	private CompositeProperty(Set<Integer> componentPropertyIds,
			Function<ListMultimap<ItemProperty, ItemPropertyValue>, CompositePropertyValue> valueFunc) {
		this.componentPropertyIds = componentPropertyIds;
		this.valueFunc = valueFunc;
	}

	public Set<Integer> getComponentPropertyIds() {
		return componentPropertyIds;
	}

	public Function<ListMultimap<ItemProperty, ItemPropertyValue>, CompositePropertyValue> getValueFunc() {
		return valueFunc;
	}
	
    private	static Set<Integer> allProperties = new HashSet<>();
    static {
    	for (CompositeProperty property : values()) {
			allProperties.addAll(property.getComponentPropertyIds());
		}
    }
	
	public static Set<Integer> propertiesCoveredByComposites() {
		return allProperties;
	}
}