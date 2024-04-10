package br.com.orcinus.orca.std.visibility

/**
 * Denotes that a structure should only be accessed from a package equal to that in which it was
 * declared or its derivatives (the child ones, those within it).
 */
@Retention(AnnotationRetention.BINARY)
@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.CLASS,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.TYPEALIAS
)
annotation class PackageProtected
