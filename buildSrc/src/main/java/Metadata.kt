object Metadata {
    const val GROUP = "com.jeanbarrossilva.mastodonte"
    const val ARTIFACT = "mastodonte"
    const val NAMESPACE = GROUP

    fun namespace(suffix: String): String {
        return "$NAMESPACE.$suffix"
    }
}
