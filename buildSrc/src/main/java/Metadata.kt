object Metadata {
    const val GROUP = "com.jeanbarrossilva.orca"
    const val ARTIFACT = "orca"
    const val NAMESPACE = GROUP

    fun namespace(suffix: String): String {
        return "$NAMESPACE.$suffix"
    }
}
