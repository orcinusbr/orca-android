package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.KSFile
import java.nio.file.Files
import java.nio.file.Path
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtImportDirective

/** [Name]s imported within this [KSFile]. **/
internal val KSFile.imports: List<Name>
    get() {
        val disposable = Disposer.newDisposable()
        return try {
            val config = CompilerConfiguration()
            val configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
            val env = KotlinCoreEnvironment.createForProduction(disposable, config, configFiles)
            val path = Path.of(filePath)
            val text = Files.readString(path)
            val virtualFile = LightVirtualFile(fileName, KotlinFileType.INSTANCE, text)
            PsiManager
                .getInstance(env.project)
                .findFile(virtualFile)
                ?.children
                ?.filterIsInstance<KtImportDirective>()
                ?.mapNotNull(KtImportDirective::importedName)
                .orEmpty()
        } finally {
            disposable.dispose()
        }
    }
