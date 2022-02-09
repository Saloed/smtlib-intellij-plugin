package com.github.saloed.pluginconfigsample.services

import com.intellij.openapi.project.Project
import com.github.saloed.pluginconfigsample.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
