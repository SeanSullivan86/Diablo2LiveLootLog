package com.d2.properties;
import java.util.Arrays;

import com.d2.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ItemProperty {
	private int id;
	private String name;
	private int numFields;
	private int[] bitsPerField;
	
	public ItemProperty(String[] row) {
		this.id = Integer.parseInt(row[2]);
		this.name = row[1];
		this.numFields = Integer.parseInt(row[6]);
		this.bitsPerField = new int[numFields];
		for (int i = 0; i < numFields; i++) {
			bitsPerField[i] = Integer.parseInt(row[7+i]);
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@JsonIgnore
	public int getNumFields() {
		return numFields;
	}

	@JsonIgnore
	public int[] getBitsPerField() {
		return bitsPerField;
	}

	@Override
	public String toString() {
		return "ItemProperty [id=" + id + ", name=" + name + ", numFields=" + numFields + ", bitsPerField="
				+ Arrays.toString(bitsPerField) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemProperty other = (ItemProperty) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public String getDescription(ItemPropertyValue value) {
		if (id == 107) {
			return "+" + value.getValue() + " to " + Skill.forId(value.getMod()).getName();
		}
		
		StringBuilder x = new StringBuilder();
		x.append(value.getValue() + " " +getName());
		
		return x.toString();
	}
	
}