package com.tianque.preloader.gradle

import org.gradle.api.Project

class AppExtension {

    Project project

    File preLoadResUrlFile
    String buildTmpDir = "/build/tmp/preloader"
    String outputDir = "src/main/assets"

    AppExtension(Project project) {
        this.project = project

    }
}
