package net.montoyo.wd.config.annoconfg.util;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

public enum EnumType {
	BYTE(byte.class),
	SHORT(short.class),
	INT(int.class),
	LONG(long.class),
	FLOAT(float.class),
	DOUBLE(double.class),
	BOOLEAN(boolean.class),
	OTHER(Object.class),
	;
	
	Class<?> clazz;
	
	EnumType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public static EnumType forClass(Class<?> clazz) {
		for (EnumType value : EnumType.values())
			if (value.clazz.equals(clazz)) return value;
		return OTHER;
	}
}
