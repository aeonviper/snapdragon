package core.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.ibatis.sqlmap.client.SqlMapClient;

import core.aspect.SqlMapClientProvider;
import core.aspect.TransactionInterceptor;
import core.aspect.Transactional;

public class TestFactory {
    
    private static final Injector injector = Guice.createInjector(new TestModule());    
    public static <T> T getInstance(Class<T> type) { return injector.getInstance(type); }
    
    public static class TestModule extends com.google.inject.AbstractModule {    
        protected void configure() {
        	bindConstant().annotatedWith(Names.named("sqlMapConfigName")).to("core/test/config/SqlMapConfig.xml");
            bind(SqlMapClient.class).toProvider(SqlMapClientProvider.class).in(Scopes.SINGLETON);
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), new TransactionInterceptor());
        }
    }
    
}
