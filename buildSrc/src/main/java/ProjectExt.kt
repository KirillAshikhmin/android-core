import org.gradle.api.Project
import java.util.*

fun Project.loadVersionProperties(): VersionProperties {
    val versionProperties = Properties()
    val file = project.rootProject.file("version.properties")
    versionProperties.load(file.inputStream())
    return VersionProperties(
        versionProperties.getProperty("major").toInt(),
        versionProperties.getProperty("minor").toInt(),
        versionProperties.getProperty("patch").toInt()
    )
}

data class VersionProperties(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val versionCode: Int = major * 1_000_000 + minor * 1_000 + patch,
    val versionName: String = "$major.$minor.$patch"
)