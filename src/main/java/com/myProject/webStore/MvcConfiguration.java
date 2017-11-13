package com.myProject.webStore;

import com.myProject.webStore.domain.Cart;
import com.myProject.webStore.domain.Product;
import com.myProject.webStore.interceptor.AuditingInterceptor;
import com.myProject.webStore.interceptor.PerformanceMonitorInterceptor;
import com.myProject.webStore.interceptor.PromoCodeInterceptor;
import com.myProject.webStore.resolvers.Jaxb2MarshallingXmlViewResolver;
import com.myProject.webStore.resolvers.JsonViewResolver;
import com.myProject.webStore.resolvers.XmlViewResolver;
import com.myProject.webStore.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.validation.Validator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;


import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@EnableConfigurationProperties(MultipartProperties.class)
@ConditionalOnClass({ WebMvcProperties.Servlet.class, StandardServletMultipartResolver.class,
		MultipartConfigElement.class })
@ConditionalOnProperty(prefix = "spring.http.multipart", name = "enabled", matchIfMissing = true)
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.myProject.webStore.controller" })
public class MvcConfiguration extends WebMvcConfigurerAdapter
{
	@Autowired
	PerformanceMonitorInterceptor performanceMonitorInterceptor;
	@Autowired
	AuditingInterceptor auditingInterceptor;
	@Autowired
	PromoCodeInterceptor promoCodeInterceptor;
	@Autowired
	MessageSource messageSource;

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };


	/**
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
	 */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}


	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(
				MediaType.TEXT_HTML);
	}

	/*
     * Configure ContentNegotiatingViewResolver
     */
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(manager);

		// Define all possible view resolvers
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();

		resolvers.add(jaxb2MarshallingXmlViewResolver());
		resolvers.add(xmlViewResolver());
		resolvers.add(jsonViewResolver());

		resolvers.add(jspViewResolver());

		resolver.setViewResolvers(resolvers);
		return resolver;
	}

	@Bean
	public ViewResolver jaxb2MarshallingXmlViewResolver() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Product.class);
		return new Jaxb2MarshallingXmlViewResolver(marshaller);
	}

	@Bean
	public ViewResolver xmlViewResolver() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Product.class);
		return new XmlViewResolver(marshaller);
	}

	@Bean
	public ViewResolver jsonViewResolver() {
		return new JsonViewResolver();
	}



	@Bean
	public ViewResolver jspViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		super.configurePathMatch(configurer);

		configurer.setUseSuffixPatternMatch(false);
		UrlPathHelper pathHelper = new UrlPathHelper();
		//Enable matrix variable
		pathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(pathHelper);
	}

	private final MultipartProperties multipartProperties;

	public MvcConfiguration(MultipartProperties multipartProperties) {
		this.multipartProperties = multipartProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public MultipartConfigElement multipartConfigElement() {
		return this.multipartProperties.createMultipartConfig();
	}

	@Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
	@ConditionalOnMissingBean(MultipartResolver.class)
	public StandardServletMultipartResolver multipartResolver() {
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
		return multipartResolver;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("pl"));
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("language");
		return lci;
	}

@Bean
public Validator validator() {
	LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
	factory.setValidationMessageSource(messageSource);
	return factory;
}


	@Bean
	public PromoCodeInterceptor promoCodeInterceptor() {
		PromoCodeInterceptor promoCodeInterceptor = new PromoCodeInterceptor();
		promoCodeInterceptor.setPromoCode("OF3RTA");
		promoCodeInterceptor.setErrorRedirect("invalidPromoCode");
		promoCodeInterceptor.setOfferRedirect("products");
		return promoCodeInterceptor;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(promoCodeInterceptor);
		registry.addInterceptor(auditingInterceptor);
		registry.addInterceptor(performanceMonitorInterceptor);
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Autowired
	private WebFlowConfig webFlowConfig;



	@Bean
	public FlowHandlerMapping flowHandlerMapping() {
		FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
		handlerMapping.setOrder(-1);
		handlerMapping.setFlowRegistry(this.webFlowConfig.flowRegistry());
		return handlerMapping;
	}

	@Bean
	public FlowHandlerAdapter flowHandlerAdapter() {
		FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
		handlerAdapter.setFlowExecutor(this.webFlowConfig.flowExecutor());
		handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
		return handlerAdapter;
	}


	/**
	 * Configure TilesConfigurer.
	 */
//	@Bean
//	public TilesConfigurer tilesConfigurer(){
//		TilesConfigurer tilesConfigurer = new TilesConfigurer();
//		tilesConfigurer.setDefinitions(new String[] {"/WEB-INF/tiles/definition/tile-definition.xml"});
//		tilesConfigurer.setCheckRefresh(true);
//		return tilesConfigurer;
//	}
//
//	/**
//	 * Configure ViewResolvers to deliver preferred views.
//	 */
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		TilesViewResolver viewResolver = new TilesViewResolver();
//		registry.viewResolver(viewResolver);
//	}


}
