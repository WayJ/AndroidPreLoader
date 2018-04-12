package com.tianque.fastnetloader.gradle

import org.gradle.api.Project

class AppExtension {

    Project project

    File preLoadResUrlFile
    String outputDir = "/src/main/assets/preLoaded"

    AppExtension(Project project) {
        this.project = project

    }
}
