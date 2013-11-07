package net.clonecomputers.lab.util;

import java.lang.reflect.*;
import java.util.*;

import org.reflections.*;
import org.reflections.scanners.*;
import org.reflections.util.*;

public class ReflectionsHelper {
	public static <T> Set<Class<? extends T>> findAllImplementations(Class<T> superclass){
		return findAllImplementations(superclass, false);
	}
	
	public static <T> Set<Class<? extends T>> findAllImplementations(Class<T> superclass,
			boolean includeAbstractClassesAndInterfaces){
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
                       
        Reflections ref = new Reflections(new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))));
        
		HashSet<Class<? extends T>> allImpl = new HashSet<Class<? extends T>>();
		Set<Class<? extends T>> allClasses = ref.getSubTypesOf(superclass);
		for(Class<? extends T> c: allClasses){
			if(superclass.isAssignableFrom(c) && !c.isAnonymousClass() &&
					includeAbstractClassesAndInterfaces || (
						!c.isInterface() &&
						!Modifier.isAbstract(c.getModifiers())
					)){
				allImpl.add(c);
			}
		}
		return allImpl;
	}
}
