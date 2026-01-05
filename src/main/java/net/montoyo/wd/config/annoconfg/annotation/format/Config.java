package net.montoyo.wd.config.annoconfg.annotation.format;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import net.minecraftforge.fml.config.ModConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	ModConfig.Type type();
	String path() default "";
}
