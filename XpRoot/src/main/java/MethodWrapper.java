import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.MethodReference;

import java.util.List;
import java.util.Set;

public class MethodWrapper implements Method {
    private final Method method;

    public MethodWrapper(Method method) {
        this.method = method;
    }

    @NotNull
    @Override
    public String getDefiningClass() {
        return method.getDefiningClass();
    }

    @NotNull
    @Override
    public String getName() {
        return method.getName();
    }

    @NotNull
    @Override
    public List<? extends CharSequence> getParameterTypes() {
        return method.getParameterTypes();
    }

    @NotNull
    @Override
    public List<? extends MethodParameter> getParameters() {
        return method.getParameters();
    }

    @NotNull
    @Override
    public String getReturnType() {
        return method.getReturnType();
    }

    @Override
    public int compareTo(@NotNull MethodReference o) {
        return method.compareTo(o);
    }

    @Override
    public int getAccessFlags() {
        return method.getAccessFlags();
    }

    @NotNull
    @Override
    public Set<? extends Annotation> getAnnotations() {
        return method.getAnnotations();
    }

    @NotNull
    @Override
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return method.getHiddenApiRestrictions();
    }

    @Nullable
    @Override
    public MethodImplementation getImplementation() {
        return method.getImplementation();
    }

    @Override
    public void validateReference() throws InvalidReferenceException {
        method.validateReference();
    }
}
