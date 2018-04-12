package com.tianque.fastnetloader.gradle

import com.tianque.fastnetloader.gradle.task.CollectPreloadResTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

class AppPlugin implements Plugin<Project> {
    protected Project project
    protected AppExtension extension

    @Override
    void apply(Project project) {
        this.project = project

        createExtension()
        createTask(project)
    }


    protected void createExtension() {
        // Add the 'small' extension object
        extension = project.extensions.create('fastnetloader', AppExtension, project)
    }

    protected void updateExtension() {
        extension = project.extensions.findByName('fastnetloader')
    }

    protected void createTask(Project project) {
        project.afterEvaluate {
            if (!project.android) {
                throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
            }

            updateExtension()

            project.tasks.find{
                it.name.contains("preBuild")
            }.each {
                def buildTmpDic = project.file(getExtension().buildTmpDir)
                def zipTargetFile = project.file(getExtension().outputDir)
                CollectPreloadResTask collectPreloadResTask = project.tasks.create("collectPreLoadResources", CollectPreloadResTask) {
                    appExtension = getExtension()
                    outputDic = buildTmpDic
                }
                collectPreloadResTask.group = "fastNetLoader"

                def zipTask = project.tasks.create("transformZipResources", Zip){
                    from(buildTmpDic.path)
                    archiveName 'preLoaded.zip'
                    destinationDir zipTargetFile
                }
//                zipTask.from(getExtension().buildTmpDir)
                zipTask.group = "fastNetLoader"
                zipTask.dependsOn(collectPreloadResTask)
                it.dependsOn(zipTask)
            }
        }

    }

    protected AppExtension getExtension() {
        return this.extension
    }


}