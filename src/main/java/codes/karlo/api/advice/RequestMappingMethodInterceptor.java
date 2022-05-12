package codes.karlo.api.advice;

import lombok.extern.apachecommons.CommonsLog;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommonsLog
public class RequestMappingMethodInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMappingMethodInterceptor.class);

    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        final StringBuilder builder = new StringBuilder();

        builder.append("Method invoked = ").append(methodInvocation.getThis().getClass().getName())
                .append(".").append(methodInvocation.getMethod().getName()).append(" (");
        final Object[] methodArguments = methodInvocation.getArguments();

        if (methodArguments.length != 0) {
            String prefix = "";
            for (final Object methodArgument : methodArguments) {
                builder.append(prefix);
                prefix = ",";
                builder.append(methodArgument.toString());
            }
        }
        builder.append(")");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(builder.toString());
        }
        return methodInvocation.proceed();
    }

}
