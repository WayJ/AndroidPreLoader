package com.tianque.fastnetloader.gradle

import org.gradle.api.Project

class AppExtension {

    Project project

    File preLoadResUrlFile
    String buildTmpDir = "/build/tmp/fastnetloader"
    String outputDir = "src/main/assets"

    AppExtension(Project project) {
        this.project = project

    }
}
