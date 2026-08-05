package farn.jarmodagent.annotations;

public @interface CLocalWithType {

    /**
     * The name of the local variable found in the local variable table.<br>
     * The local variable table is optional in the class file but is required for the name resolution.<br>
     * Use the index if no local variable table is present.
     *
     * @return The name of the local variable
     */
    String name() default "";

    /**
     * The ordinal of the local variable to get from the local variable table.<br>
     * The ordinal is counted for every type of local variable (e.g. I, L, Ljava/lang/String;, ...).<br>
     * Use the index if no local variable table is present.
     *
     * @return The ordinal
     */
    int ordinal() default -1;

    /**
     * The var index of the local variable to get.
     *
     * @return The var index
     */
    int index() default -1;

    /**
     * Specifies the type of this local.
     */
    Class<?> type() default void.class;

    /**
     * Copy the value of the local variable back to the caller method.<br>
     * When marking a local variable as modifiable the type of the method parameter is deciding the type of the stored local variable.<br>
     * <b>You can use {@link Object} to get any non-primitive variable, but it will be stored back as an {@link Object} and not as the original type.<br>
     * This may cause a {@link ClassCastException} when not careful.</b>
     *
     * @return If the local variable should be copied back
     */
    boolean modifiable() default false;
}
