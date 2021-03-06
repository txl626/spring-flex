package org.springframework.flex.core.io;


import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.flex.core.AbstractMessageBrokerTests;
import org.springframework.flex.core.io.domain.EmbeddedAddress;
import org.springframework.flex.core.io.domain.Person;
import org.springframework.flex.core.io.domain.PersonNP;

import flex.messaging.io.PropertyProxyRegistry;


public class ClassPathScanningAmfConversionServiceConfigProcessorTests extends AbstractMessageBrokerTests {

    public void testBasicPackageScan() throws Exception {
        setDirty();
        ClassPathScanningAmfConversionServiceConfigProcessor configProcessor = new ClassPathScanningAmfConversionServiceConfigProcessor("org.springframework.flex.core.io.domain");
        configProcessor.setBeanClassLoader(getApplicationContext().getClassLoader());
        configProcessor.setResourceLoader(getApplicationContext());
        configProcessor.afterPropertiesSet();
        
        addStartupProcessor(configProcessor);
        getMessageBroker();
        
        assertNotNull(PropertyProxyRegistry.getProxy(new Person()));
        assertTrue(PropertyProxyRegistry.getProxy(new Person()) instanceof SpringPropertyProxy);
    }
    
    public void testPackageScanWithRegexIncludeFilter() throws Exception {
        setDirty();
        ClassPathScanningAmfConversionServiceConfigProcessor configProcessor = new ClassPathScanningAmfConversionServiceConfigProcessor("org.springframework.flex.core.io.domain");
        configProcessor.setBeanClassLoader(getApplicationContext().getClassLoader());
        configProcessor.setResourceLoader(getApplicationContext());
        configProcessor.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*NP")));
        configProcessor.afterPropertiesSet();
        
        addStartupProcessor(configProcessor);
        getMessageBroker();
        
        assertNotNull(PropertyProxyRegistry.getProxy(new Person()));
        assertFalse(PropertyProxyRegistry.getProxy(new Person()) instanceof SpringPropertyProxy);
        assertNotNull(PropertyProxyRegistry.getProxy(new PersonNP()));
        assertTrue(PropertyProxyRegistry.getProxy(new PersonNP()) instanceof SpringPropertyProxy);
    }
    
    public void testPackageScanWithRegexExcludeFilter() throws Exception {
        setDirty();
        ClassPathScanningAmfConversionServiceConfigProcessor configProcessor = new ClassPathScanningAmfConversionServiceConfigProcessor("org.springframework.flex.core.io.domain");
        configProcessor.setBeanClassLoader(getApplicationContext().getClassLoader());
        configProcessor.setResourceLoader(getApplicationContext());
        configProcessor.addExcludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*NP")));
        configProcessor.afterPropertiesSet();
        
        addStartupProcessor(configProcessor);
        getMessageBroker();
        
        assertNotNull(PropertyProxyRegistry.getProxy(new Person()));
        assertTrue(PropertyProxyRegistry.getProxy(new Person()) instanceof SpringPropertyProxy);
        assertNotNull(PropertyProxyRegistry.getProxy(new PersonNP()));
        assertFalse(PropertyProxyRegistry.getProxy(new PersonNP()) instanceof SpringPropertyProxy);
    }
    
    public void testPackageScanWithAnnotationIncludeFilter() throws Exception {
        setDirty();
        ClassPathScanningAmfConversionServiceConfigProcessor configProcessor = new ClassPathScanningAmfConversionServiceConfigProcessor("org.springframework.flex.core.io.domain");
        configProcessor.setBeanClassLoader(getApplicationContext().getClassLoader());
        configProcessor.setResourceLoader(getApplicationContext());
        configProcessor.addIncludeFilter(new AnnotationTypeFilter(Embeddable.class));
        configProcessor.afterPropertiesSet();
        
        addStartupProcessor(configProcessor);
        getMessageBroker();
        
        assertNotNull(PropertyProxyRegistry.getProxy(new Person()));
        assertFalse(PropertyProxyRegistry.getProxy(new Person()) instanceof SpringPropertyProxy);
        assertNotNull(PropertyProxyRegistry.getProxy(new EmbeddedAddress()));
        assertTrue(PropertyProxyRegistry.getProxy(new EmbeddedAddress()) instanceof SpringPropertyProxy);
    }
}
