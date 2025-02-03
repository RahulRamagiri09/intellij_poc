package com.example.sidebarplugin

import com.intellij.openapi.project.Project
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

object GitInfo {
    fun getGitInfo(project: Project): GitData? {
        return try {
            val projectPath = project.basePath ?: throw Exception("Project base path is null")

            val repoDir = File(projectPath, ".git")
            if (!repoDir.exists() || !repoDir.isDirectory) {
                throw Exception(".git directory not found in the project directory")
            }

            val repository: Repository = FileRepositoryBuilder()
                .setGitDir(repoDir)
                .readEnvironment()
                .findGitDir()
                .build()

            val branch = repository.branch
            val repoName = repository.directory.parentFile?.name ?: "Unknown"

            return GitData(repoName, branch)
        } catch (e: Exception) {
            println("Git info not available: ${e.message}")
            null
        }
    }
}

data class GitData(val repositoryName: String, val currentBranch: String)
