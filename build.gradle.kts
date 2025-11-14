plugins {
    id("cobblemon.root-conventions")
    kotlin("jvm") version "2.0.0"
}

version = "${project.property("mod_version")}+${project.property("mc_version")}"

val isSnapshot = project.property("snapshot")?.equals("true") == true
if (isSnapshot) {
    val fixedBranchName = versioning.info.branch.substringAfter("/")
    version = "$version-${fixedBranchName}-${versioning.info.build}"
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(8)
}